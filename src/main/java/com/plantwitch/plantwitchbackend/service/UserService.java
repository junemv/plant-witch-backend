package com.plantwitch.plantwitchbackend.service;

import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    post new user
    public ResponseEntity<Object> newUser(User user) {
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

//    get all user IDs
    public List<User> getAll(){
        return this.userRepository.findAll();
    }

//    get user by ID
    public Optional<User> getUser(Long id){
        return this.userRepository.findById(id);
    }
}
