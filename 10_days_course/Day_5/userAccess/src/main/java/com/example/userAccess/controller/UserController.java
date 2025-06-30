package com.example.userAccess.controller;

import com.example.userAccess.dto.UserDTO;
import com.example.userAccess.model.User;
import com.example.userAccess.repository.UserRepository;
import com.example.userAccess.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository repo; // Injects the JPA repository

    @Autowired
    private JwtUtil jwtUtil; // Utility to generate and validate JWT tokens

    @Autowired
    private PasswordEncoder passwordEncoder; // Injects password encoder(BCrypt)

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO dto) {
        // Check if username already exists
        if (repo.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Create and save the new user
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash password
        repo.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody UserDTO dto) {
//        Optional<User> userOpt = repo.findByUsername(dto.getUsername());
//
//        // Check if user exists and password matches
//        if (userOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
//            String token = jwtUtil.generateToken(dto.getUsername());
//            return ResponseEntity.ok(Map.of("token", token)); // Return JWT to client
//        }
//
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate token with role
        String token = jwtUtil.generateToken(username, user.getRole());

        return Map.of("token", token);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String adminOnly() {
        return "Only admins can access this";
    }


}


