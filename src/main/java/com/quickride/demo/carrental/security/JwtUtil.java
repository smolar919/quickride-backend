package com.quickride.demo.carrental.security;

import com.quickride.demo.carrental.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer";
    private final String KEY = "5pAq6zRyX8bC3dV2wS7gN1mK9jF0hL4tUoP6iBvE3nG8xZaQrY7cW2fA";

    private final JwtParser jwtParser;

    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(KEY);
    }

    public String generateToken(String userId, Role role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);

        Instant tokenCreateTime = Instant.now();
        Instant tokenValidity = tokenCreateTime.plus(3600000, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(tokenValidity))
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(String token) {
        return parseJwtClaims(token);
    }

    public Optional<String> resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return Optional.of(bearerToken.substring(TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }
}
