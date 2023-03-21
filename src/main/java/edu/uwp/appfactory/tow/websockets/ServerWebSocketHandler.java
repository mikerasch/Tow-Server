package edu.uwp.appfactory.tow.websockets;

import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class ServerWebSocketHandler extends TextWebSocketHandler {
    private final Map<WebSocketSession,UserDetails> sessions = new HashMap<>();

    private final LocationService locationService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public ServerWebSocketHandler(LocationService locationService, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.locationService = locationService;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession socketSession) throws IOException {
        verifyAuthorizationToken(socketSession);
    }

    private void verifyAuthorizationToken(WebSocketSession socketSession) throws IOException {
        String authToken = socketSession.getHandshakeHeaders().getFirst("Authorization");
        String userUUID = jwtUtils.getUUIDFromJwtToken(authToken);
        if(userUUID == null) {
            socketSession.close();
            return;
        }
        UserDetails userDetails = userDetailsService.loadUserByUUID(UUID.fromString(userUUID));
        if(userDetails == null) {
            socketSession.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        sessions.put(socketSession,userDetails);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession socketSession, CloseStatus status) {
        locationService.updateActiveStatus(false, sessions.get(socketSession));
        sessions.remove(socketSession);
    }
}
