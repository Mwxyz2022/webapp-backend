package com.example.auth.security;
import org.springframework.lang.NonNull;
import com.example.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final String secret;
    private final RedisTokenRepository tokenRepository;

    public JwtAuthFilter(String secret, RedisTokenRepository tokenRepository) {
        this.secret = secret;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secret.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String telegramUserId = claims.getSubject();

                // Перевірка: токен існує в Redis
                String storedToken = tokenRepository.getAccessToken(telegramUserId);
                if (storedToken == null || !storedToken.equals(token)) {
                    throw new RuntimeException("Token not found or invalidated in Redis");
                }

                // Установка користувача в SecurityContext
                var auth = new UsernamePasswordAuthenticationToken(
                        telegramUserId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
