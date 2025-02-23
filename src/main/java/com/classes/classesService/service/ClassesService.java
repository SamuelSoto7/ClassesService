package com.classes.classesService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classes.classesService.model.Classes;
import com.classes.classesService.repository.ClassesRepository;
import com.classes.classesService.dto.TrainerDTO;

@Service
public class ClassesService {

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private TrainerService trainerService;

    public Classes scheduleClass(Classes classes) {
        return classesRepository.save(classes);
    }

    public List<Classes> getAllClasses() {
        return classesRepository.findAll();
    }

    public TrainerDTO getTrainerDetails(Long trainerId) {
        return trainerService.getTrainerById(trainerId);
    }
}
