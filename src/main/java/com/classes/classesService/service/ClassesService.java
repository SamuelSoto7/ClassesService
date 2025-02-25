package com.classes.classesService.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
                dto.setTrainer(trainerMap.get(clase.getTrainerId()));
                return dto;
            })
            .collect(Collectors.toList());
    
        return classWithTrainerDtos;
    }

   
}
