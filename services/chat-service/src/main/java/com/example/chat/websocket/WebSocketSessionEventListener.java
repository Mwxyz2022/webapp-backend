package com.example.chat.websocket;

import com.example.redis.session.RedisSessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketSessionEventListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionEventListener.class);
    private final RedisSessionRegistry sessionRegistry;

    public WebSocketSessionEventListener(RedisSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String managerId = accessor.getFirstNativeHeader("managerId");

        if (managerId != null && sessionId != null) {
            sessionRegistry.saveSession(sessionId, managerId);
            log.info("🔌 WebSocket CONNECTED: sessionId={}, managerId={}", sessionId, managerId);
        } else {
            log.warn("⚠️ CONNECT без managerId або sessionId. Пропущено.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        if (sessionId != null) {
            sessionRegistry.removeSession(sessionId);
            log.info("🔌 WebSocket DISCONNECTED: sessionId={}", sessionId);
        }
    }
}
