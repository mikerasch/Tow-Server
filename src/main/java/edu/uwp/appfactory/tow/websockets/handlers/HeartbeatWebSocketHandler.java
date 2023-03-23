package edu.uwp.appfactory.tow.websockets.handlers;

import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.websockets.VerifyAuthorizationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Configuration
public class HeartbeatWebSocketHandler extends TextWebSocketHandler {
    private final Map<WebSocketSession,UserDetails> sessions = new HashMap<>();
    private final LocationService locationService;
    private final VerifyAuthorizationToken verifyAuthorizationToken;
    public HeartbeatWebSocketHandler(LocationService locationService,VerifyAuthorizationToken verifyAuthorizationToken) {
        this.locationService = locationService;
        this.verifyAuthorizationToken = verifyAuthorizationToken;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession socketSession) throws IOException {
        UserDetails userdetails = verifyAuthorizationToken.verifyAuthorizationToken(socketSession);
        if(userdetails == null) {
            socketSession.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        sessions.put(socketSession,userdetails);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession socketSession, CloseStatus status) {
        locationService.updateActiveStatus(false, sessions.get(socketSession));
        sessions.remove(socketSession);
    }
}
