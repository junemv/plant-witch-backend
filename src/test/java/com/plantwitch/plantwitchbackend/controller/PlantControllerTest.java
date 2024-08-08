package com.plantwitch.plantwitchbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.service.PlantService;
import lombok.Lombok;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private Plant testPlant1;
    private User testUser;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testPlant = new Plant();
        testUser = new User();
        testUser.setId((1L));
        testPlant.setId(1L);
        testPlant.setName("My testing plant.");
        testPlant.setImage("test_image.jpg");
        testPlant.setDescription("A test description.");
        testPlant.setRepotDate("2024-07-27");
        testPlant.setWaterDate("2024-07-27");
        testPlant.setWaterInterval(6);
        testPlant.setRepotInterval(12);
        testPlant.setUser(testUser);

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

        when(plantRepository.findById(plantId)).thenReturn(Optional.of(testPlant));
        when(plantRepository.save(any(Plant.class))).thenReturn(testPlant);

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "My new plant name.");
        updates.put("description", "A new description.");

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

        verify(plantRepository,times(1)).save(testPlant);
    }

    @Test
    public void testDeletePlant() throws Exception {
        Long plantId = 1L;

        when(plantRepository.findById(plantId)).thenReturn(Optional.of(testPlant));

        mockMvc.perform(delete("/api/v1/plants/delete/{id}", plantId))
                .andExpect(status().isNoContent());

        verify(plantRepository, times(1)).delete(testPlant);
    }

    @Test
    public void testDeletePlantReturnsNotFoundIfPlantIdIsNotFound() throws Exception {
        Long plantId = 1L;

        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/plants/delete/{id}", plantId))
                .andExpect(status().isNotFound());

        verify(plantRepository, times(0)).delete(testPlant);
    }

    @Test
    public void testGetPlantById() throws Exception {
        Long plantId = 1L;
        ObjectMapper objectMapper = new ObjectMapper();
        String plantJson = objectMapper.writeValueAsString(testPlant);

        when(plantService.getPlant(plantId)).thenReturn(Optional.of(testPlant));

        mockMvc.perform(get("/api/v1/plants/{plant_id}", plantId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(plantJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.name").value("My testing plant."))
            .andExpect(jsonPath("$.waterDate").value("2024-07-27"))
            .andExpect(jsonPath("$.repotDate").value("2024-07-27"))
            .andExpect(jsonPath("$.waterInterval").value(6))
            .andExpect(jsonPath("$.repotInterval").value(12));

    }

    @Test void testGetAllPlantsByUser() throws Exception {

        Long userId = 5L;
        Long plantId = 1L;
        testPlant1 = new Plant();
        Long plantId1 = 2L;
        testPlant1.setId(2L);
        testPlant1.setName("My second testing plant.");
        testPlant1.setImage("test_second_image.jpg");
        testPlant1.setDescription("A test second description.");
        testPlant1.setRepotDate("2024-07-28");
        testPlant1.setWaterDate("2024-07-28");
        testPlant1.setWaterInterval(7);
        testPlant1.setRepotInterval(10);
        testPlant1.setUser(testUser);

        List<Plant> plantsList = List.of(testPlant, testPlant1);

        when(plantService.getAllPlantsByUser(userId)).thenReturn(plantsList);

        mockMvc.perform(get("/api/v1/plants/users/{user_id}", userId))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(plantService, times(1)).getAllPlantsByUser(userId);
    }
    }

    @Test
    public void testGetPlantSchedule() throws Exception {
        Long plantId = 1L;

        when(plantRepository.findById(plantId)).thenReturn(Optional.of(testPlant));
        when(plantService.calculateDaysUntilNextAction(testPlant.getWaterDate(),testPlant.getWaterInterval())).thenReturn(7L);
        when(plantService.calculateDaysUntilNextAction(testPlant.getRepotDate(), testPlant.getRepotInterval())).thenReturn(16L);

        mockMvc.perform(get("/api/v1/plants/{id}/schedule", plantId))
                .andDo(print())  // Log the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daysUntilNextWatering").value(7L))
                .andExpect(jsonPath("$.daysUntilNextRepotting").value(16L));
    }

    @Test
    public void testGetPlantScheduleReturnsNotFoundIfPlantIdIsNotFound() throws Exception {
        Long plantId = 1L;

        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/plants/{id}/schedule", plantId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testResetWaterDate() throws Exception {
        Long plantId = 1L;
        String updatedWaterDate = LocalDate.now().toString();
        testPlant.setWaterDate(updatedWaterDate);

        when(plantRepository.findById(plantId)).thenReturn(Optional.of(testPlant));
        when(plantRepository.save(any(Plant.class))).thenReturn(testPlant);

        mockMvc.perform(patch("/api/v1/plants/{id}/water-date", plantId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.waterDate").value(updatedWaterDate));

        verify(plantRepository,times(1)).save(testPlant);
    }

    @Test
    public void testResetWaterDateReturnsNotFoundIfPlantIdIsNotFound() throws Exception {
        Long plantId = 1L;
        String updatedWaterDate = LocalDate.now().toString();
        testPlant.setWaterDate(updatedWaterDate);

        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/plants/{id}/water-date", plantId))
                .andExpect(status().isNotFound());

        verify(plantRepository,times(0)).save(testPlant);
    }

    @Test
    public void testResetRepotDate() throws Exception {
        Long plantId = 1L;
        String updatedRepotDate = LocalDate.now().toString();
        testPlant.setRepotDate(updatedRepotDate);

        when(plantRepository.findById(plantId)).thenReturn(Optional.of(testPlant));
        when(plantRepository.save(any(Plant.class))).thenReturn(testPlant);

        mockMvc.perform(patch("/api/v1/plants/{id}/repot-date", plantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repotDate").value(updatedRepotDate));

        verify(plantRepository,times(1)).save(testPlant);
    }

    @Test
    public void testResetRepotDateReturnsNotFoundIfPlantIdIsNotFound() throws Exception {
        Long plantId = 1L;
        String updatedRepotDate = LocalDate.now().toString();
        testPlant.setRepotDate(updatedRepotDate);

        when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/plants/{id}/repot-date", plantId))
                .andExpect(status().isNotFound());

        verify(plantRepository,times(0)).save(testPlant);
    }
}
