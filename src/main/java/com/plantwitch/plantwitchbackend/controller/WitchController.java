package com.plantwitch.plantwitchbackend.controller;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;
import com.plantwitch.plantwitchbackend.service.WitchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/witch_ai")
@CrossOrigin(origins = "https://plant-witch-frontend.vercel.app/")
public class WitchController {
    @Autowired
    private WitchRepository witchRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PlantRepository plantRepository;

    @Autowired
    private WitchService witchService;

    @PostMapping("/ask_witch/{user_id}")
    public ResponseEntity<WitchAIResponse> askWitchAI(@PathVariable Long user_id, @RequestBody List<Map<String, String>> chatHistoryBody) {
        String lastPrompt = chatHistoryBody.get(chatHistoryBody.size() - 1).get("content");
        WitchAIResponse savedAIResponse = witchService.newWitchQuery(user_id, chatHistoryBody, lastPrompt);

        return ResponseEntity.ok(savedAIResponse);
    }

    @PatchMapping("/{witch_id}")
    public ResponseEntity<WitchAIResponse> saveWitchAIToPlant(@PathVariable Long witch_id, @RequestBody Map<String, Long> plantId) {
        Long plant_id = plantId.get("plant_id");
        WitchAIResponse witchAIResponse = witchService.SaveAIResponseToPlant(plant_id, witch_id);
        return ResponseEntity.ok(witchAIResponse);
    }

    //Gets all witch responses for provided plant
    @GetMapping("/plants/{plant_id}")
    public ResponseEntity<List<WitchAIResponse>> getAllAIResponsesForOnePlant (@PathVariable Long plant_id) {
        List<WitchAIResponse> witchAIResponses = witchService.getAllAIResponsesForPlant(plant_id);
        return ResponseEntity.ok(witchAIResponses);
    }

    //Gets all witch responses for provided user
    @GetMapping("/users/{user_id}")
    public ResponseEntity<List<WitchAIResponse>> getAllAIResponsesForOneUser (@PathVariable Long user_id) {
        List<WitchAIResponse> witchAIResponses = witchService.getAllAIResponsesForUser(user_id);
        return ResponseEntity.ok(witchAIResponses);
    }
}



