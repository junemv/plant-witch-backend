package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class PlantServiceTest {
@Mock
    private PlantRepository plantRepository;

@Mock
    private UserRepository userRepository;

@InjectMocks
    private PlantService plantService;
    private AutoCloseable closeable;
    private Plant testPlant;
    private User testUser;

@BeforeEach
void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
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

@AfterEach
void tearDown() throws Exception {
    closeable.close();
}

@Test
void testCreatePlant() {
    Long userId = testUser.getId();

    when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
    when(plantRepository.save(testPlant)).thenReturn(testPlant);

    Plant createdPlant = plantService.newPlant(userId, testPlant);

    assertThat(createdPlant).isNotNull();
    assertThat(createdPlant.getName()).isEqualTo("My testing plant.");
    assertThat(createdPlant.getRepotDate()).isEqualTo("2024-07-27");
    assertThat(createdPlant.getRepotInterval()).isEqualTo(12);
    assertThat(createdPlant.getWaterDate()).isEqualTo("2024-07-27");
    assertThat(createdPlant.getWaterInterval()).isEqualTo(6);
    assertThat(createdPlant.getUser().getId()).isEqualTo(userId);
}

    @Test
    void testCalculateDaysUntilNextAction() {
        //given
        String actionDate = LocalDate.now().minusDays(2).toString();
        int actionInterval = 10;

        //when
        long daysUntilNextAction = plantService.calculateDaysUntilNextAction(actionDate, actionInterval);

        //then
        assertEquals(8, daysUntilNextAction);
    }

    @Test
    void testCalculateDaysUntilNextActionWhenOverdue() {
        //given
        String actionDate = LocalDate.now().minusDays(7).toString();
        int actionInterval = 3;

        //when
        long daysUntilNextAction = plantService.calculateDaysUntilNextAction(actionDate, actionInterval);

        //then
        assertEquals(-4, daysUntilNextAction);
    }

    @Test
    void testCalculatesZeroDaysUntilNextAction() {
        //given
        String actionDate = LocalDate.now().minusDays(5).toString();
        int actionInterval = 5;

        //when
        long daysUntilNextAction = plantService.calculateDaysUntilNextAction(actionDate, actionInterval);

        //then
        assertEquals(0, daysUntilNextAction);
    }

    @Test
    void testUpdatePlantIsSuccessful() {
        when(plantRepository.findById(1L)).thenReturn(Optional.of(testPlant));
        when(plantRepository.save(testPlant)).thenReturn(testPlant);

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "My new plant name.");
        updates.put("commonName", "A new common name.");
        updates.put("description", "A new description.");
        updates.put("waterInterval","30");
        updates.put("repotInterval","60");

        Optional<Plant> updatedPlant = plantService.updatePlant(1L, updates);

        assertTrue(updatedPlant.isPresent());
        assertEquals("My new plant name.", updatedPlant.get().getName());
        assertEquals("A new description.", updatedPlant.get().getDescription());
        assertEquals(30, updatedPlant.get().getWaterInterval());
        assertEquals(60, updatedPlant.get().getRepotInterval());
    }
}
