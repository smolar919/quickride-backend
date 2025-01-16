package com.quickride.demo.carrental.security;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService service;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(UserDetailsService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessTokenOptional = jwtUtil.resolveToken(request);
            if (accessTokenOptional.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            String accessToken = accessTokenOptional.get();
            Claims claims = jwtUtil.resolveClaims(accessToken);

            if (claims != null && jwtUtil.validateClaims(claims)) {
                String id = claims.getSubject();
                JwtAuthUser userDetails = (JwtAuthUser) service.loadUserByUsername(id);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
        filterChain.doFilter(request, response);
    }
}