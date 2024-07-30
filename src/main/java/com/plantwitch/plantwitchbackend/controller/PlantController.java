package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin
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

    @PatchMapping("/{id}/water-date")
    public ResponseEntity<Plant> resetWaterDate(@PathVariable Long id) {
        Optional<Plant> plantOptional = plantRepository.findById(id);
        if ((plantOptional.isPresent())) {
            Plant plant = plantOptional.get();
            plant.setWaterDate(LocalDate.now().toString());
            Plant updatedPlant = plantRepository.save(plant);
            return ResponseEntity.ok(updatedPlant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/repot-date")
    public ResponseEntity<Plant> resetRepotDate(@PathVariable Long id) {
        Optional<Plant> plantOptional = plantRepository.findById(id);
        if ((plantOptional.isPresent())) {
            Plant plant = plantOptional.get();
            plant.setRepotDate(LocalDate.now().toString());
            Plant updatedPlant = plantRepository.save(plant);
            return ResponseEntity.ok(updatedPlant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
