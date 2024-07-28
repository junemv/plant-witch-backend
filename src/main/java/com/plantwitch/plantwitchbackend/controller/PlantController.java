package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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

    @GetMapping("/{plant_id}")
    public Optional<Plant> getPlantById(@PathVariable Long plant_id) {
        return plantService.getPlant(plant_id);
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<List<Plant>> getAllPlantsByUser(@PathVariable Long user_id) {
        List<Plant> plants = plantService.getAllPlantsByUser(user_id);
        return ResponseEntity.ok(plants);
    }


    @PostMapping("/users/{user_id}")
    public ResponseEntity<Plant> createPlant(@PathVariable Long user_id, @RequestBody Plant plant) {
        Plant savedPlant = plantService.newPlant(user_id, plant);

        return ResponseEntity.ok(savedPlant);
    }

    @GetMapping("/{id}/watering")
    public ResponseEntity<String> getPlantWateringSchedule(@PathVariable Long id) {
        Optional<Plant> plantOptional = plantRepository.findById(id);
        if (plantOptional.isPresent()) {
            Plant plant = plantOptional.get();
            long daysUntilNextWatering = plantService.calculateDaysUntilNextWatering(plant.getWaterDate(), plant.getWaterInterval());
            String response = "Water due in " + daysUntilNextWatering +" days.";
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}