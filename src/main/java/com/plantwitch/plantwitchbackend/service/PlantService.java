package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.user.User;
import com.plantwitch.plantwitchbackend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
