package com.quickride.demo.carrental.security;

import org.springframework.stereotype.Component;


@Component
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;


    public JwtTokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String generateToken(String userId) {
        return jwtUtil.generateToken(userId);
    }
}