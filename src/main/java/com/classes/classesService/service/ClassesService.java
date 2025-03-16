package com.classes.classesService.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.classes.classesService.dto.ClassTrainerDto;
import com.classes.classesService.dto.TrainerDto;
import com.classes.classesService.model.Classes;
import com.classes.classesService.repository.ClassesRepository;

@Service
public class ClassesService {

    @Autowired
    private ClassesRepository classesRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private Map<String, String> classOccupancyMap = new ConcurrentHashMap<>();
    
    @Value("${trainer.service.url}")
    private String trainerServiceUrl;

    public Classes scheduleClass(Classes classes) {
        TrainerDto trainer = Optional.ofNullable(
                restTemplate.getForObject(trainerServiceUrl + "/trainers/" + classes.getTrainerId(), TrainerDto.class)
        ).orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + classes.getTrainerId()));
    
        return classesRepository.save(classes);
    }


    public List<ClassTrainerDto> getAllClasses() {
        List<Classes> classes = classesRepository.findAll();
    
        String url = trainerServiceUrl + "/trainers";
        ResponseEntity<TrainerDto[]> response = restTemplate.getForEntity(url, TrainerDto[].class);
        List<TrainerDto> trainers = Arrays.asList(response.getBody());
    
        Map<Long, TrainerDto> trainerMap = trainers.stream().collect(Collectors.toMap(TrainerDto::getId, trainer -> trainer));
    
        List<ClassTrainerDto> classWithTrainerDtos = classes.stream()
            .map(clase -> {
                ClassTrainerDto dto = new ClassTrainerDto();
                dto.setId(clase.getId());
                dto.setName(clase.getName());
                dto.setHorary(clase.getHorary());
                dto.setMaxCapacity(clase.getMaxCapacity());
                dto.setCurrentOccupancy(clase.getCurrentOccupancy());
                dto.setTrainer(trainerMap.get(clase.getTrainerId()));
                return dto;
            })
            .collect(Collectors.toList());
    
        return classWithTrainerDtos;
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendOccupancyUpdate(Long classId, int currentOccupancy) {
        // Primero, actualizamos la ocupación de la clase en la base de datos.
        Optional<Classes> claseOpt = classesRepository.findById(classId);
        if (claseOpt.isPresent()) {
            Classes clase = claseOpt.get();
            clase.setCurrentOccupancy(currentOccupancy);  // Utilizamos setCurrentOccupancy para actualizar la ocupación
            classesRepository.save(clase);  // Guardar la clase con la nueva ocupación
        } else {
            throw new RuntimeException("Clase no encontrada con ID: " + classId);
        }
    
        // Luego, enviamos el mensaje al topic de Kafka.
        String message = "Class ID: " + classId + " - Occupancy: " + currentOccupancy;
        kafkaTemplate.send("ocupacion-clases", message);
    }
    
    

    @KafkaListener(topics = "ocupacion-clases", groupId = "my-consumer-group")
    public void listenToClassOccupancy(String message) {
        System.out.println("Occupancy Update: " + message);
        String classId = message.split(" - ")[0].split(": ")[1];
        String occupancy = message.split(" - ")[1].split(": ")[1];
        classOccupancyMap.put(classId, occupancy);

    }

    public String getClassOccupancy(String classId) {
        return classOccupancyMap.getOrDefault(classId, "No data available");
    }
}
