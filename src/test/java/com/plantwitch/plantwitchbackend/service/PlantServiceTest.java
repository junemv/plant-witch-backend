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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
