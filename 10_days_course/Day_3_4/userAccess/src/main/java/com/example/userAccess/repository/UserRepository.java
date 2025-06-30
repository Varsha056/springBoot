package com.example.userAccess.repository;

import com.example.userAccess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public int register(String username, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, username, hashedPassword);
    }
public  boolean login(String username, String password){
//        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
// for bcrypt
    String sql = "SELECT password FROM users WHERE username = ?";
        try{
            String hashedPassword = jdbcTemplate.queryForObject(sql,String.class,username);
            return passwordEncoder.matches(password, hashedPassword);
        }catch (Exception e) {
            return false;
        }
//        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,username,password);
//        return count!=null && count>0;
    }

}
