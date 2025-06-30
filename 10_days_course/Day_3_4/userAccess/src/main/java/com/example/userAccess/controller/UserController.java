package com.example.userAccess.controller;

import com.example.userAccess.model.User;
import com.example.userAccess.repository.UserRepository;
import com.example.userAccess.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    // POST: /api/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        int rows = repo.register(user.getUsername(),user.getPassword());
        if (rows > 0) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }

    // POST: /api/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean valid = repo.login(user.getUsername(), user.getPassword());
        if (valid) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Welcome to your Dashboard!");
    }

    // GET: /api/profile (JWT protected)
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("Here is your profile data.");
    }











}
