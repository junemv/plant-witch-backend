package com.plantwitch.plantwitchbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest (PlantController.class)
public class PlantControllerTest {

    @MockBean
    private PlantService plantService;

    @MockBean
    private PlantRepository plantRepository;

    private Plant testPlant;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testPlant = new Plant();
        testPlant.setId(1L);
        testPlant.setName("My testing plant.");
        testPlant.setImage("test_image.jpg");
        testPlant.setDescription("A test description.");
        testPlant.setRepotDate("2024-07-27");
        testPlant.setWaterDate("2024-07-27");
        testPlant.setWaterInterval(6);
        testPlant.setRepotInterval(12);

    }

    @Test
    public void testCreatePlant() throws Exception {
        Long userId = 777L;

        when(plantService.newPlant(anyLong(), any(Plant.class))).thenReturn(testPlant);

        ObjectMapper objectMapper = new ObjectMapper();
        String plantJson = objectMapper.writeValueAsString(testPlant);

        mockMvc.perform(post("/api/v1/plants/users/{user_id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(plantJson))
                .andDo(print())  // Log the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My testing plant."))
                .andExpect(jsonPath("$.waterDate").value("2024-07-27"))
                .andExpect(jsonPath("$.repotDate").value("2024-07-27"))
                .andExpect(jsonPath("$.waterInterval").value(6))
                .andExpect(jsonPath("$.repotInterval").value(12));
    }

    @Test
    public void testUpdatePlantNameAndDescription() throws Exception {
        Long plantId = 1L;

        when(plantRepository.findById(anyLong())).thenReturn(Optional.of(testPlant));
        when(plantRepository.save(any(Plant.class))).thenReturn(testPlant);

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "My new plant name.");
        updates.put("description","A new description.");

        ObjectMapper objectMapper = new ObjectMapper();
        String plantJson = objectMapper.writeValueAsString(updates);

        mockMvc.perform(patch("/api/v1/plants/updates/{id}", plantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(plantJson))
                .andDo(print())  // Log the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My new plant name."))
                .andExpect(jsonPath("$.description").value("A new description."))
                .andExpect(jsonPath("$.image").value("test_image.jpg"))
                .andExpect(jsonPath("$.waterDate").value("2024-07-27"))
                .andExpect(jsonPath("$.repotDate").value("2024-07-27"))
                .andExpect(jsonPath("$.waterInterval").value(6))
                .andExpect(jsonPath("$.repotInterval").value(12));
    }
}