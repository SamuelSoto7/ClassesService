package com.classes.classesService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.classes.classesService.model.Classes;

public interface ClassesRepository extends JpaRepository<Classes, Long> {}
