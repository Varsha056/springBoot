package com.example.userAccess.controller;

import com.example.userAccess.model.User;
import com.example.userAccess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository repo;

    @PostMapping("/register")
    public  String register(@RequestBody User user){
        int result = repo.register(user);
        return  result > 0 ?"Registered":"Failed";
    }

    @PostMapping("/login")
    public  String login(@RequestBody User user){
        boolean isValid = repo.login((user.getUsername()),user.getPassword());
        return isValid ? "Logged in": "Invalid cred";
    }











}
