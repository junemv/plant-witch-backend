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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PlantServiceTest {
@Mock
    private PlantRepository plantRepository;

@Mock
    private UserRepository userRepository;

@InjectMocks
    private PlantService plantService;
    private AutoCloseable closeable;

@BeforeEach
void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
}

@AfterEach
void tearDown() throws Exception {
    closeable.close();
}

@Test
void testCreatePlant() {
    User user = new User();
    Plant plant = new Plant();
    Long userId = 1L;
    user.setId(userId);
    plant.setUser(user);
    plant.setName("Test Plant Name");
    plant.setRepotDate("2024-07-27");
    plant.setRepotInterval(12);
    plant.setWaterDate("2024-07-27");
    plant.setWaterInterval(6);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(plantRepository.save(plant)).thenReturn(plant);

    Plant createdPlant = plantService.newPlant(userId, plant);

    assertThat(createdPlant).isNotNull();
    assertThat(createdPlant.getName()).isEqualTo("Test Plant Name");
    assertThat(createdPlant.getRepotDate()).isEqualTo("2024-07-27");
    assertThat(createdPlant.getRepotInterval()).isEqualTo(12);
    assertThat(createdPlant.getWaterDate()).isEqualTo("2024-07-27");
    assertThat(createdPlant.getWaterInterval()).isEqualTo(6);
    assertThat(createdPlant.getUser().getId()).isEqualTo(userId);
}

@Test
void testGetPlantById() {
    Plant plant = new Plant();
    plant.setId(1L);
    plant.setName("Test Plant");
    Long plantId = 1L;

    when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

    Optional<Plant> foundPlant = plantService.getPlant(plantId);

    assertTrue(foundPlant.isPresent());
    assertEquals("Test Plant", foundPlant.get().getName());

}

    @Test
    void testGetAllPlantsByUserId_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            plantService.getAllPlantsByUser(userId);
        });

        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }

    @Test
    void testGetAllPlantsByUserId() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Plant plant1 = new Plant();
        plant1.setId(1L);
        plant1.setName("Test Plant");
        plant1.setUser(user);

        Plant plant2 = new Plant();
        plant2.setId(2L);
        plant2.setName("Test Plant2");
        plant2.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // Mock the user repository
        when(plantRepository.findByUserId(userId)).thenReturn(List.of(plant1, plant2));

        List<Plant> foundPlants = plantService.getAllPlantsByUser(userId);

        assertFalse(foundPlants.isEmpty());
        assertEquals("Test Plant", foundPlants.get(0).getName());
        assertEquals("Test Plant2", foundPlants.get(1).getName());

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

}
