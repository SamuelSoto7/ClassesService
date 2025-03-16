package com.classes.classesService.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassTrainerDto {
    private Long id;
    private String name;
    private LocalDateTime horary;
    private int maxCapacity;
    private int currentOccupancy;
    private TrainerDto trainer;
}
