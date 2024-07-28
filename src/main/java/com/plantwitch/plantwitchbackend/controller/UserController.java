package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
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

//    Get all user IDs
//    TODO - deprecate when user auth is implemented. Temporary solution to switch accounts for demo purposes
//    TODO - NOTE - this route uses a toString() method for readability, you may want to remove it when working with this data in your code
    @GetMapping("/all")
    public String getAllUserId(){
        return userService.getAll().toString();
    }

//    Post one user
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user){
        return userService.newUser(user);
    }
}
