package com.classes.classesService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classes.classesService.dto.ClassTrainerDto;
import com.classes.classesService.model.Classes;
import com.classes.classesService.service.ClassesService;

@RestController
@RequestMapping("/api/classes")
public class ClassesController {
    @Autowired
    private ClassesService classesService;

    @PostMapping
    public Classes scheduleClass(@RequestBody Classes classes) {
        return classesService.scheduleClass(classes);
    }

    @GetMapping
    public List<ClassTrainerDto> getAllClasses() {
        return classesService.getAllClasses();
    }

}
