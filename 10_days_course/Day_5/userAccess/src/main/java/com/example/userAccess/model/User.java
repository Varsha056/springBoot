package com.example.userAccess.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Mark this class as a JPA entity that will map to a database table
@Entity
@Table(name = "users") // Explicitly sets the table name in the DB as "users"
@Getter
@Setter
public class User {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments ID in MySQL
    private Long id;

    @Column(nullable = false, unique = true) // Can't be null and must be unique in the table
    private String username;

    private String password; // Will be stored as BCrypt-hashed

    private String role = "USER"; // Default role; can be extended to ADMIN, etc.
//
//    private String status = "ACTIVE"; // User account status (ACTIVE, BLOCKED, etc.)


}
