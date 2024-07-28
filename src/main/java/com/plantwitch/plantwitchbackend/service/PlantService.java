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

@Service
public class PlantService {
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository, UserRepository userRepository) {

        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

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

    public long calculateDaysUntilNextWatering(String waterDate, int waterInterval) {
        DateTimeFormatter stringToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentWaterDate = LocalDate.parse(waterDate, stringToDate);

        LocalDate nextWaterDate = currentWaterDate.plusDays(waterInterval);

        LocalDate today = LocalDate.now();
        long daysUntilNextWatering = ChronoUnit.DAYS.between(today, nextWaterDate);

        return daysUntilNextWatering;

    }

    // return one plant by ID
    public Optional<Plant> getPlant(Long id){
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

}

