package com.example.auth.security;

import com.example.common.exceptions.TokenNotFoundException;
import com.example.common.utils.JwtUtil;
import com.example.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;
    private final RedisTokenRepository tokenRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, RedisTokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
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
                Claims claims = jwtUtil.parseToken(token);
                String telegramUserId = claims.getSubject();

                String storedToken = tokenRepository.getAccessToken(telegramUserId);
                if (storedToken == null || !storedToken.equals(token)) {
                    log.warn("❌ Token for user {} не знайдено в Redis або не співпадає", telegramUserId);
                    throw new TokenNotFoundException();
                }

                var auth = new UsernamePasswordAuthenticationToken(
                        telegramUserId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

                log.debug("✅ Аутентифікація успішна для користувача {}", telegramUserId);

            } catch (TokenNotFoundException e) {
                log.warn("❌ Авторизація неуспішна: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            } catch (Exception ex) {
                log.error("❌ Помилка при обробці JWT токена", ex);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        } else {
            log.debug("🔒 Authorization header відсутній або не починається з Bearer");
        }

        filterChain.doFilter(request, response);
    }
}
