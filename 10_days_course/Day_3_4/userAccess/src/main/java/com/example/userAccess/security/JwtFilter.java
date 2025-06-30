package com.example.userAccess.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends GenericFilter {

    @Autowired
    private  JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest) req;
        String authHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();

        // ✅ Skip filter for public endpoints
        if (path.equals("/api/login") || path.equals("/api/register")) {
            chain.doFilter(req, res);
            return;
        }

        if(authHeader!= null && authHeader.startsWith("Bearer")){
            String jwt = authHeader.substring(7);
            System.out.println("Token valid? " + jwtUtil.validToken(jwt));
            System.out.println("Extracted user: " + jwtUtil.extractUsername(jwt));

            if(jwtUtil.validToken(jwt)){
                String userName = jwtUtil.extractUsername(jwt);
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        chain.doFilter(req, res);

    }
}
