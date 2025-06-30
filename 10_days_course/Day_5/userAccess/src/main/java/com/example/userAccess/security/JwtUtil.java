package com.example.userAccess.security;

import io.jsonwebtoken.*; // Core JWT API
import io.jsonwebtoken.security.Keys; // For generating signing key
import org.springframework.beans.factory.annotation.Value; // Inject values from properties
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Makes this a Spring Bean
public class JwtUtil {

    // ✅ Inject secret key from application.properties (you must set jwt.secret)
    @Value("${jwt.secret}")
    private String secret;

    // ✅ Token expiration time — 24 hours (in milliseconds)
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    // ✅ Convert the secret string into a secure HMAC key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ✅ Generate a JWT token for a given username (acts as subject)
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)                        // Store username in subject
                .claim("role", role)
                .setIssuedAt(new Date())                     // Current time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // Expiry time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign using secret key
                .compact();                                  // Return token as String
    }

    // ✅ Extract username from token by reading "subject" claim
    public String extractUsername(String token) {
        return getClaims(token).getSubject(); // Subject = username
    }

    // ✅ Validate token: ensures it's not expired & properly signed
    public boolean validToken(String token) {
        try {
            Claims claims = getClaims(token);               // Extract claims
            return !claims.getExpiration().before(new Date()); // Ensure token is not expired
        } catch (Exception e) {
            return false; // Token is invalid (could be expired, tampered, or malformed)
        }
    }

    // ✅ Parse the token and extract all claims (data inside JWT)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())             // Use the same signing key
                .build()
                .parseClaimsJws(token)                      // Decode and verify the token
                .getBody();                                 // Return the JWT payload
    }
}
