package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {
    private final PlantRepository plantRepository;
    private final PlantService plantService;

    @Autowired
    public PlantController(PlantRepository plantRepository, PlantService plantService) {

        this.plantRepository = plantRepository;
        this.plantService = plantService;
    }

    @PostMapping("/users/{user_id}")
    public ResponseEntity<Plant> createPlant(@PathVariable Long user_id, @RequestBody Plant plant) {
        Plant savedPlant = plantService.newPlant(user_id, plant);
        return ResponseEntity.ok(savedPlant);
    }

}
