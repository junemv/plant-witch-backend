package com.plantwitch.plantwitchbackend.repository;

import com.plantwitch.plantwitchbackend.entity.WitchAIResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WitchRepository extends JpaRepository<WitchAIResponse, Long> {
}
