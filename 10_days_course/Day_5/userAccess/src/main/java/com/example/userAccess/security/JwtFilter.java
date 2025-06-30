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
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI(); // Get the current URL path
        System.out.println("JWT Filter Path: " + path);
        System.out.println("Authorization Header: " + request.getHeader("Authorization"));



        // ✅ Skip filtering for public endpoints
        if (path.equals("/api/login") || path.equals("/api/register")) {
            chain.doFilter(req, res);
            return;
        }

        // 🔐 Check for JWT Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7); // Remove "Bearer " prefix

            if (jwtUtil.validToken(jwt)) {
                String username = jwtUtil.extractUsername(jwt);

                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(
                                username, null, Collections.emptyList());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        chain.doFilter(req, res); // Continue with the filter chain
    }

}
