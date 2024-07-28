package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.service.PlantService;
import com.plantwitch.plantwitchbackend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class PlantControllerTest {

    @Mock
    private PlantService plantService;

    @InjectMocks
    private PlantController plantController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(plantController).build();
    }

    @Test
    public void createPlant() throws Exception {
        User user = new User();
        Plant plant = new Plant();
        Long userId = 1L;
        user.setId(userId);
        plant.setUser(user);
        plant.setId(1L);
        plant.setName("Test Plant Name");
        plant.setRepotDate("2024-07-27");
        plant.setRepotInterval(12);
        plant.setWaterDate("2024-07-27");
        plant.setWaterInterval(6);
        plant.setImage("image.jpg");

        when(plantService.newPlant(userId, plant)).thenReturn(plant);

        String plantJson = "{\"user_id\":1, \"name\":\"Test Plant Name\",\"waterDate\":\"2024-07-27\",\"repotDate\":\"2024-07-27\",\"waterInterval\":7,\"repotInterval\":12, \"id\":1}";

        mockMvc.perform(post("/api/v1/plants/users/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(plantJson))
                .andDo(print())  // Log the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Plant Name"))
                .andExpect(jsonPath("$.waterDate").value("2024-07-27"))
                .andExpect(jsonPath("$.repotDate").value("2024-07-27"))
                .andExpect(jsonPath("$.waterInterval").value(6))
                .andExpect(jsonPath("$.repotInterval").value(12));

    }
}