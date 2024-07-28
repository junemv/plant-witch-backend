package com.plantwitch.plantwitchbackend.repository;

import com.plantwitch.plantwitchbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
