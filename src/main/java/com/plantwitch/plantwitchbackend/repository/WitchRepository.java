package com.plantwitch.plantwitchbackend.repository;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WitchRepository extends JpaRepository<WitchAIResponse, Long> {
    List<WitchAIResponse> findByPlantId(Long plant_id);
    List<WitchAIResponse> findByUserId(Long user_id);
}
