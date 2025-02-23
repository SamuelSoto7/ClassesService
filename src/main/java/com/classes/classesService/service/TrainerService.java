package com.classes.classesService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.classes.classesService.dto.TrainerDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final RestTemplate restTemplate;

    @Value("${trainer.service.url}") 
    private String trainerServiceUrl;

    public TrainerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TrainerDTO getTrainerById(Long trainerId) {
        // Llamamos al endpoint que devuelve todos los entrenadores
        String url = trainerServiceUrl + "/trainers";
        List<TrainerDTO> trainers = Arrays.asList(restTemplate.getForObject(url, TrainerDTO[].class));

        // Buscamos el entrenador con el ID correcto
        Optional<TrainerDTO> trainer = trainers.stream()
                                               .filter(t -> t.getId().equals(trainerId))
                                               .findFirst();

        return trainer.orElse(null); // Devuelve null si no encuentra el entrenador
    }
}
