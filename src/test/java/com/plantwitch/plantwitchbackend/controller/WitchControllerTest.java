package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import com.plantwitch.plantwitchbackend.repository.WitchRepository;
import com.plantwitch.plantwitchbackend.service.WitchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest (WitchController.class)
public class WitchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WitchService witchService;

    @MockBean
    private WitchRepository witchRepository;

    @MockBean
    private UserRepository userRepository;

//    @Test
//    public void testAskWitchAI() throws Exception {
//        WitchAIResponse response = new WitchAIResponse();
//        response.setId(1L);
//        response.setResponse("A test response.");
//        response.setPrompt("How much water does basil need?");
//
//        when(witchService.newWitchQuery(anyLong(), anyString())).thenReturn(response);
//
//        String questionJson = "{\"prompt\":\"How much water does basil need?\"}";
//
//        mockMvc.perform(post("/api/v1/witch_ai/ask_witch/{user_id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8")
//                        .content(questionJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.prompt").value("How much water does basil need?"))
//                .andExpect(jsonPath("$.response").value("A test response."));
//    }
}
