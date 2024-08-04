package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.OpenAIResponse;
import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WitchService {
    private final UserRepository userRepository;
    private final WitchRepository witchRepository;

    @Autowired
    public WitchService(WitchRepository witchRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.witchRepository = witchRepository;
    }

    //
    //Saves newly created plants
    @Transactional
    public WitchAIResponse newWitchQuery(Long user_id, String prompt) {
        Optional<User> userOptional = userRepository.findById(user_id);
        WitchAIResponse witch = new WitchAIResponse();
        String apiKey = "";
        OpenAIResponse openAIResponse = callOpenAI(prompt, apiKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            witch.setUser(user);
            witch.setPrompt(prompt);
            witch.setTimestamp(LocalDateTime.now());
            witch.setResponse(openAIResponse.getChoices().get(0).getMessage().getContent());

            return witchRepository.save(witch);
        } else {
            throw new RuntimeException("User not found with ID: " + user_id);
        }

    }

    private OpenAIResponse callOpenAI(String prompt, String apiKey) {
        // Make API call to OpenAI
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
}
