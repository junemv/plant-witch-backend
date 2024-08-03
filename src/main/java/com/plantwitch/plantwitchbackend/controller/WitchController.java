package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.entity.OpenAIResponse;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/witch_ai")
public class WitchController {
    @Autowired
    private WitchRepository witchRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/ask_witch")
    public WitchAIResponse askWitchAI(@RequestBody Map<String, String> promptBody) {
        String prompt = promptBody.get("prompt");
        String apiKey = "";
//        Long userId = Long.parseLong(promptBody.get("userId"));
        Long userId = 1L;

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Call to OpenAI API
        OpenAIResponse openAIResponse = callOpenAI(prompt, apiKey);
        WitchAIResponse witchAIResponse = new WitchAIResponse();
        witchAIResponse.setPrompt(prompt);
        witchAIResponse.setResponse(openAIResponse.getChoices().get(0).getMessage().getContent());
        witchAIResponse.setTimestamp(LocalDateTime.now());
        witchAIResponse.setUser(user);

        return witchRepository.save(witchAIResponse);
    }

    private OpenAIResponse callOpenAI(String prompt, String apiKey) {
        // Make API call to OpenAI
        String openApiUrl = "https://api.openai.com/v1/chat/completions";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo"); //we can choose other like 4 mini
        request.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        request.put("prompt", prompt);
        request.put("max_tokens", 150);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAIResponse> response = restTemplate.postForEntity(openApiUrl, requestEntity, OpenAIResponse.class);
        return response.getBody();
    }
}



