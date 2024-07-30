package com.plantwitch.plantwitchbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantwitch.plantwitchbackend.controller.PlantController;
import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.fasterxml.jackson.databind.ObjectMapper.*;

@Service
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final ObjectMapper jacksonObjectMapper;


    //
    @Autowired
    public PlantService(PlantRepository plantRepository, UserRepository userRepository, ObjectMapper jacksonObjectMapper) {

        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

//    @Autowired
    private PlantController plantController;

    @Transactional
    public Plant newPlant(Long user_id, Plant plant) {
        Optional<User> userOptional = userRepository.findById(user_id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            plant.setUser(user);
            return plantRepository.save(plant);
        } else {
            throw new RuntimeException("User not found with ID: " + user_id);
        }

    }

    public long calculateDaysUntilNextAction(String date, int interval) {
        DateTimeFormatter stringToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.parse(date, stringToDate);

        LocalDate nextActionDate = currentDate.plusDays(interval);

        LocalDate today = LocalDate.now();
        long daysUntilNextAction = ChronoUnit.DAYS.between(today, nextActionDate);

        return daysUntilNextAction;

    }

    // return one plant by ID
    public Optional<Plant> getPlant(Long id) {
        return this.plantRepository.findById(id);
    }

    // return all plants for one user by ID
    public List<Plant> getAllPlantsByUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            return plantRepository.findByUserId(user_id);
        }
        throw new RuntimeException("User not found with ID: " + user_id);
    }

    // get a list of dictionaries with common name and photos of all plants
    public List<Map<String, String>> getAllPlantsWithId() {
        List<Map<String, String>> plantsMapList = new ArrayList<>();
        try {
            ResponseEntity<String> plantsResponse = plantController.getSpeciesList();

            if (plantsResponse != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(plantsResponse.getBody());
                JsonNode dataNode = rootNode.get("data");
                HashMap<String, String> plantMap = new HashMap<>();

                for (JsonNode plant : dataNode) {
                    String commoName = plant.get("common_name").asText();
                    JsonNode imageNode = plant.get("default_image");
                    String imageUrl = imageNode.get("small_url").asText();
                    plantMap.put(commoName, imageUrl);
                    plantsMapList.add(plantMap);
                }
                System.out.println(plantsMapList);
                return plantsMapList;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return plantsMapList;
    }

}

