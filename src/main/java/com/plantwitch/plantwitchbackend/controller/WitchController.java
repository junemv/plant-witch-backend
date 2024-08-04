package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.entity.OpenAIResponse;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;
import com.plantwitch.plantwitchbackend.service.WitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/witch_ai")
public class WitchController {
    @Autowired
    private WitchRepository witchRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WitchService witchService;

    @PostMapping("/ask_witch/{user_id}")
    public ResponseEntity<WitchAIResponse> askWitchAI(@PathVariable Long user_id, @RequestBody Map<String, String> promptBody) {
        String prompt = promptBody.get("prompt");
        WitchAIResponse savedAIResponse = witchService.newWitchQuery(user_id, prompt);

        return ResponseEntity.ok(savedAIResponse);
//        String apiKey = "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";
//        Long userId = Long.parseLong(promptBody.get("userId"));
//        Long userId = 1L;
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()) {
//            throw new RuntimeException("User not found with ID: " + userId);
//        }
//        User user = userOptional.get();

        // Call to OpenAI API
//        OpenAIResponse openAIResponse = callOpenAI(prompt, apiKey);
//        WitchAIResponse witchAIResponse = new WitchAIResponse();
//        witchAIResponse.setPrompt(prompt);
//        witchAIResponse.setResponse(openAIResponse.getChoices().get(0).getMessage().getContent());
//        witchAIResponse.setTimestamp(LocalDateTime.now());
//        witchAIResponse.setUser(user);

//        return witchRepository.save(witchAIResponse);
    }

//    private OpenAIResponse callOpenAI(String prompt, String apiKey) {
//        // Make API call to OpenAI
//        String openApiUrl = "https://api.openai.com/v1/chat/completions";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(apiKey);
//        Map<String, Object> request = new HashMap<>();
//        request.put("model", "gpt-4o-mini"); //we can choose other
//        request.put("messages", List.of(Map.of("role", "user", "content", prompt)));
//        request.put("max_tokens", 150);
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);
//        ResponseEntity<OpenAIResponse> response = restTemplate.postForEntity(openApiUrl, requestEntity, OpenAIResponse.class);
//        return response.getBody();
//    }
}



