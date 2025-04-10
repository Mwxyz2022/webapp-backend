package com.example.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {

    private final Key secretKey;
    private final long jwtExpirationMs;

    public JwtUtil(String secret, long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = expirationMs;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token) {
        try {
            return !extractExpiration(token).before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
