package com.quickride.demo.carrental.security;

import com.quickride.demo.carrental.model.Role;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;


    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateToken(String userId, Role role) {
        return jwtUtil.generateToken(userId, role);
    }
}