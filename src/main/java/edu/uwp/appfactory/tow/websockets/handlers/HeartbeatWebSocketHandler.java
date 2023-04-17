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

    /**
     * Callback method that is invoked after a WebSocket connection is established.
     * Verifies the authorization token of the user associated with the WebSocket session
     * by calling a helper method, and adds the user details to a HashMap that maps WebSocket
     * sessions to user details. If the token is invalid or missing, the WebSocket session is closed
     * with a policy violation close status code.
     *
     * @param socketSession the WebSocket session that was established
     * @throws IOException if an I/O error occurs while handling the WebSocket session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession socketSession) throws IOException {
        UserDetails userdetails = verifyAuthorizationToken.verifyAuthorizationToken(socketSession);
        if(userdetails == null) {
            socketSession.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        sessions.put(socketSession,userdetails);
    }

    /**
     * Callback method that is invoked after a WebSocket connection is closed.
     * Updates the active status of the user associated with the WebSocket session to false
     * by calling a helper method, and removes the session from a HashMap that maps WebSocket
     * sessions to user details.
     *
     * @param socketSession the WebSocket session that was closed
     * @param status the close status of the WebSocket session
     */
    @Override
    public void afterConnectionClosed(WebSocketSession socketSession, CloseStatus status) {
        locationService.updateActiveStatus(false, sessions.get(socketSession));
        sessions.remove(socketSession);
    }
}
