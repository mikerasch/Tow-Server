package edu.uwp.appfactory.tow.websockets.handlers;
import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.websockets.SessionInformation;
import edu.uwp.appfactory.tow.websockets.VerifyAuthorizationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Configuration
public class TowJobWebSocketHandler extends TextWebSocketHandler {
    private final VerifyAuthorizationToken verifyAuthorizationToken;
    private final Map<String, SessionInformation> webSocketSessionListMap = new HashMap<>();
    private final LocationService locationService;
    public TowJobWebSocketHandler(VerifyAuthorizationToken verifyAuthorizationToken, LocationService locationService) {
        this.verifyAuthorizationToken = verifyAuthorizationToken;
        this.locationService = locationService;
    }

    /**
     * Callback method that is invoked after a WebSocket connection is established.
     * Verifies the authorization token of the user associated with the WebSocket session
     * by calling a helper method, and adds the user details to the current session by
     * calling a separate helper method. If the token is invalid or missing, the WebSocket
     * session is closed with a policy violation close status code.
     *
     * @param socketSession the WebSocket session that was established
     * @throws IOException if an I/O error occurs while handling the WebSocket session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession socketSession) throws IOException {
        UserDetails userDetails = verifyAuthorizationToken.verifyAuthorizationToken(socketSession);
        if(userDetails == null) {
            socketSession.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        addToCurrentSession(socketSession, userDetails);
    }

    /**
     * Adds the specified user details to the current session associated with the given WebSocket session.
     * If a session with the same URL ID already exists in the internal map, the user details and WebSocket session
     * are added to that session. Otherwise, a new session is created and added to the map with the given user details
     * and WebSocket session.
     *
     * @param socketSession the WebSocket session associated with the current session
     * @param userDetails the user details to add to the current session
     */
    private void addToCurrentSession(WebSocketSession socketSession, UserDetails userDetails) {
        String sessionUrlId = getSocketSessionId(socketSession);
        if(!webSocketSessionListMap.containsKey(sessionUrlId)) {
           List<UserDetails> userDetailsList = new ArrayList<>();
           userDetailsList.add(userDetails);
           List<WebSocketSession> webSocketSessionList = new ArrayList<>();
           webSocketSessionList.add(socketSession);
           webSocketSessionListMap.put(sessionUrlId, new SessionInformation(
                   webSocketSessionList,
                   userDetailsList
           ));
        }
        else {
            SessionInformation sessionInformation = webSocketSessionListMap.get(sessionUrlId);
            sessionInformation.userDetails().add(userDetails);
            sessionInformation.webSocketSessions().add(socketSession);
            webSocketSessionListMap.put(sessionUrlId, sessionInformation);
        }
    }

    /**
     * Called after a WebSocket connection is closed. Closes all active WebSocket sessions for the given
     * `socketSession` and updates the Tow Truck Driver job status associated with the user details stored in the
     * `SessionInformation` object.
     *
     * @param socketSession The WebSocketSession that was closed.
     * @param status        The CloseStatus of the WebSocket connection.
     * @throws IOException if an I/O error occurs while closing the WebSocket sessions.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession socketSession, CloseStatus status) throws IOException {
        SessionInformation sessionInformation = webSocketSessionListMap.get(getSocketSessionId(socketSession));
        for(WebSocketSession webSocketSession: sessionInformation.webSocketSessions()) {
            webSocketSession.close();
        }
        updateTowTruckDriverJobStatus(sessionInformation.userDetails());
        webSocketSessionListMap.remove(getSocketSessionId(socketSession));
    }

    /**
     * Updates the Tow Truck Driver job status associated with the given list of `userDetails`.
     * If a Tow Truck Driver is found in the list of `userDetails`, their active status will be updated to false.
     *
     * @param userDetails A list of UserDetails objects representing the users to check for Tow Truck Driver status.
     */
    private void updateTowTruckDriverJobStatus(List<UserDetails> userDetails) {
        for(UserDetails users: userDetails) {
            if (users.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TCUSER"))) {
                locationService.updateActiveStatus(false, users);
                return;
            }
        }
    }

    /**
     * Called when a WebSocket receives a text message. Sends the received message to all active WebSocket sessions
     * associated with the same `socketSessionId` as the given `session`.
     *
     * @param session The WebSocketSession that received the message.
     * @param message The TextMessage received by the WebSocket.
     * @throws Exception if an error occurs while sending the message to the WebSocket sessions.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        List<WebSocketSession> webSocketSessions = webSocketSessionListMap.get(getSocketSessionId(session)).webSocketSessions();
        for(WebSocketSession webSocketSession: webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }

    /**
     * Returns the unique ID associated with the given `session`. This ID is extracted from the URI of the WebSocket
     * session, which is assumed to contain the ID as the last component of the path.
     *
     * @param session The WebSocketSession for which to retrieve the ID.
     * @return The ID associated with the WebSocketSession.
     */
    public String getSocketSessionId(WebSocketSession session) {
        URI uri = session.getUri();
        String url;
        if(uri != null) {
            url = uri.toString();
            String[] split = url.split("/");
            return split[split.length - 1];
        }
        return "";
    }
}
