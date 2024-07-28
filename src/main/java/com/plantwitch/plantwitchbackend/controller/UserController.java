package com.plantwitch.plantwitchbackend.controller;

import com.plantwitch.plantwitchbackend.entity.User;
import com.plantwitch.plantwitchbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TODO - Figure out how to assign user ID to the end of the endpoint
@RequestMapping("plantwitch/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody User user){
        return userService.newUser(user);
    }
}
