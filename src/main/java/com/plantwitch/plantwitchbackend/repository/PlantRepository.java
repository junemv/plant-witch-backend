package com.plantwitch.plantwitchbackend.repository;

import com.plantwitch.plantwitchbackend.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long>{
    List<Plant> findByUserId(Long user_id);
}


