package com.plantwitch.plantwitchbackend.user;

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

//    post new user route response
    public ResponseEntity<Object> newUser(User user) {
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

//    get all user IDs response
    public List<User> getAll(){
        return this.userRepository.findAll();
    }

//    get user by ID response
    public Optional<User> getUser(Long id){
        return this.userRepository.findById(id);
    }
}
