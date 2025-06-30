package com.example.userAccess.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("MySuperSecretKeyThatIsAtLeast32Chars!".getBytes());
    private  final long EXPIRATION = 1000*60*60; //1 hour

    public String generateToken(String userName){
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validToken(String token){
            try {
                Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (ExpiredJwtException e) {
                System.out.println("❌ Token expired");
            } catch (SignatureException e) {
                System.out.println("❌ Signature invalid");
            } catch (JwtException e) {
                System.out.println("❌ Invalid JWT: " + e.getMessage());
            }
            return false;


    }
}
