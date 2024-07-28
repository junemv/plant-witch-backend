package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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

    @PostMapping("/users/{user_id}")
    public ResponseEntity<Plant> createPlant(@PathVariable Long user_id, @RequestBody Plant plant) {
        Plant savedPlant = plantService.newPlant(user_id, plant);

        return ResponseEntity.ok(savedPlant);
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<Map<String, Long>> getPlantSchedule(@PathVariable Long id) {
        Optional<Plant> plantOptional = plantRepository.findById(id);
        if (plantOptional.isPresent()) {
            Plant plant = plantOptional.get();
            long daysUntilNextWatering = plantService.calculateDaysUntilNextAction(plant.getWaterDate(), plant.getWaterInterval());
            long daysUntilNextRepotting = plantService.calculateDaysUntilNextAction(plant.getRepotDate(), plant.getRepotInterval());

            Map<String, Long> schedule = new HashMap<>();
            schedule.put("daysUntilNextWatering", daysUntilNextWatering);
            schedule.put("daysUntilNextRepotting", daysUntilNextRepotting);

            return ResponseEntity.ok(schedule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
