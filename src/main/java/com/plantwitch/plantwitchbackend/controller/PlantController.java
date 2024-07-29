package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/plants")

public class PlantController {
    private final PlantRepository plantRepository;
    private final PlantService plantService;
    private final HttpClient httpClient;


    @Autowired
    public PlantController(PlantRepository plantRepository, PlantService plantService, HttpClient httpClient) {

        this.plantRepository = plantRepository;
        this.plantService = plantService;
        this.httpClient = httpClient;
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

    @GetMapping("/species-list")
    public ResponseEntity<String> getSpeciesList() {
        try {
            String url = "https://perenual.com/api/species-list?key=sk-y6bd66a7fd00b2eb36377";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return ResponseEntity.ok(response.body());
        } catch (Exception e) {
//            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while fetching species list.");
        }
    }

}
