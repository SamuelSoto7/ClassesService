package com.classes.classesService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classes.classesService.dto.ClassTrainerDto;
import com.classes.classesService.model.Classes;
import com.classes.classesService.service.ClassesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/classes")
public class ClassesController {
    @Autowired
    private ClassesService classesService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
        summary = "Schedule a new class",
        description = "This endpoint allows an ADMIN to schedule a new class. " +
                      "The class details must be provided in the request body."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Class scheduled successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden, ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public Classes scheduleClass(@RequestBody Classes classes) {
        Classes scheduledClass = classesService.scheduleClass(classes);
        // Enviar un mensaje de ocupación inicial (0 ocupantes) al topic "ocupacion-clases"
        classesService.sendOccupancyUpdate(classes.getId(), 0);  // Se inicia con 0 ocupantes
        return scheduledClass;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @Operation(
        summary = "Get all classes",
        description = "This endpoint retrieves a list of all classes. " +
                      "It can be accessed by users with either the ADMIN or TRAINER role."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of classes retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden, ADMIN or TRAINER role required"),
        @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    public List<ClassTrainerDto> getAllClasses() {
        return classesService.getAllClasses();
    }

    @PostMapping("/update-occupancy")
    public ResponseEntity<String> updateOccupancy(@RequestParam Long classId, @RequestParam int currentOccupancy) {
        classesService.sendOccupancyUpdate(classId, currentOccupancy);
        return ResponseEntity.ok("Occupancy update sent");
    }

    @GetMapping("/class-occupancy/{classId}")
    public ResponseEntity<String> getClassOccupancy(@PathVariable String classId) {
        String occupancy = classesService.getClassOccupancy(classId);
        return ResponseEntity.ok(occupancy); 
    }


}
