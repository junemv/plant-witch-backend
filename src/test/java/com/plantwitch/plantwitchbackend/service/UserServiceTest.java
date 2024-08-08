package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.Plant;
import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import jakarta.inject.Inject;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
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
    void testCreateUser() {
        User user = new User();
        Long userId = 1L;
        user.setId(userId);
        user.setEmail("test@test.com");
        user.setFirstName("Plant");
        user.setLastName("Witch");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.newUser(user);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo("test@test.com");
        assertThat(createdUser.getFirstName()).isEqualTo("Plant");
        assertThat(createdUser.getLastName()).isEqualTo("Witch");
    }

    @Test
    public void testGetUserById() throws Exception {}

    @Test
    public void testGetAllUsers() throws Exception{}

}
