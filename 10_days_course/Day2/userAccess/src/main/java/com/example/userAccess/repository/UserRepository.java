package com.example.userAccess.repository;

import com.example.userAccess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public  int register(User user){
        String sql = "Insert INTO users(username,password) VALUES(?,?)";
        return jdbcTemplate.update(sql, user.getUsername(),user.getPassword());
    }

public  boolean login(String username, String password){
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,username,password);
        return count!=null && count>0;
    }

}
