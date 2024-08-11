package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.OpenAIResponse;
import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@Service
public class WitchService {
    private final UserRepository userRepository;
    private final WitchRepository witchRepository;
    private final PlantRepository plantRepository;

    @Autowired
    public WitchService(WitchRepository witchRepository, UserRepository userRepository, PlantRepository plantRepository) {
        this.userRepository = userRepository;
        this.witchRepository = witchRepository;
        this.plantRepository = plantRepository;
    }


    //Saves a new witch response to db
    @Transactional
    public WitchAIResponse newWitchQuery(Long user_id, String promptText) {
        Optional<User> userOptional = userRepository.findById(user_id);
        WitchAIResponse witch = new WitchAIResponse();
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            OpenAIResponse openAIResponse = callOpenAI(promptText, apiKey);
            witch.setPrompt(promptText);
            witch.setTimestamp(LocalDateTime.now());
            witch.setResponse(openAIResponse.getChoices().get(0).getMessage().getContent());
            witch.setUser(user);

            return witchRepository.save(witch);
        } else {
            throw new RuntimeException("User not found with ID: " + user_id);
        }

    }
    // Make API call to OpenAI
    private OpenAIResponse callOpenAI(String prompt, String apiKey) {
        String openApiUrl = "https://api.openai.com/v1/chat/completions";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4o-mini"); //we can choose other
        request.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        request.put("max_tokens", 150);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAIResponse> response = restTemplate.postForEntity(openApiUrl, requestEntity, OpenAIResponse.class);
        return response.getBody();
    }

    // Save AI response to a specific Plant
    public WitchAIResponse SaveAIResponseToPlant(Long plant_id, Long witch_id) {
        Optional<Plant> plantOptional = plantRepository.findById(plant_id);
        Optional<WitchAIResponse> witchAIResponseOptional = witchRepository.findById(witch_id);
        if (plantOptional.isPresent() && witchAIResponseOptional.isPresent()) {
            Plant plant = plantOptional.get();
            WitchAIResponse witch = witchAIResponseOptional.get();
            witch.setPlant(plant);
            return witchRepository.save(witch);
        } else {
            throw new RuntimeException("Plant or Witch AI response not found");
        }
    }

    //Returns all witch responses for one plant by its id
    public List<WitchAIResponse> getAllAIResponsesForPlant(Long plant_id) {
        Optional<Plant> plant = plantRepository.findById(plant_id);
        if (plant.isPresent()) {
            List<WitchAIResponse> witchAIResponses = witchRepository.findByPlantId(plant_id);
            return witchAIResponses.stream()
                    .sorted(Comparator.comparing(WitchAIResponse::getId))
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Plant not found with ID: " + plant_id);
    }

    //Returns all witch responses for one user by its id
    public List<WitchAIResponse> getAllAIResponsesForUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            List<WitchAIResponse> witchAIResponses = witchRepository.findByUserId(user_id);
            return witchAIResponses.stream()
                    .sorted(Comparator.comparing(WitchAIResponse::getId))
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("User not found with ID: " + user_id);
    }
}
