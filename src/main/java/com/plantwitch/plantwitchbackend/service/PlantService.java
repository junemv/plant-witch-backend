package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository, UserRepository userRepository) {

        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    //Saves newly created plants
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

    //Returns one plant by its ID
    public Optional<Plant> getPlant(Long id){
        return this.plantRepository.findById(id);
    }

    //Returns all plants for one user by user ID
    public List<Plant> getAllPlantsByUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            List<Plant> plants = plantRepository.findByUserId(user_id);
            return plants.stream()
                    .sorted(Comparator.comparing(Plant::getId))
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("User not found with ID: " + user_id);
    }

}

