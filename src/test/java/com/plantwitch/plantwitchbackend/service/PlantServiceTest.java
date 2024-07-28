package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.repository.PlantRepository;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class PlantServiceTest {
@Mock
    private PlantRepository plantRepository;

@Mock
    private UserRepository userRepository;

@InjectMocks
    private PlantService plantService;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
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
}
