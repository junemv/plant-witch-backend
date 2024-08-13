package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://plant-witch-frontend.vercel.app/")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    Get one user by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.getUser(id);
    }

//    Get all users
//    TODO - deprecate when user auth is implemented. Temporary solution to switch accounts for demo purposes
    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAll();
    }

//    Post one user
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user){
        return userService.newUser(user);
    }
}
