package com.plantwitch.plantwitchbackend.controller;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;
import com.plantwitch.plantwitchbackend.service.WitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


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
    }
}



