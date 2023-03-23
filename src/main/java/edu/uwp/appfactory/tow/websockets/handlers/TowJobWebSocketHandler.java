package edu.uwp.appfactory.tow.websockets.handlers;
import edu.uwp.appfactory.tow.websockets.SessionInformation;
import edu.uwp.appfactory.tow.websockets.VerifyAuthorizationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Configuration
public class TowJobWebSocketHandler extends TextWebSocketHandler {
    private final VerifyAuthorizationToken verifyAuthorizationToken;
    private final Map<String, SessionInformation> webSocketSessionListMap = new HashMap<>();
    public TowJobWebSocketHandler(VerifyAuthorizationToken verifyAuthorizationToken) {
        this.verifyAuthorizationToken = verifyAuthorizationToken;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession socketSession) throws IOException {
        UserDetails userDetails = verifyAuthorizationToken.verifyAuthorizationToken(socketSession);
        if(userDetails == null) {
            socketSession.close(CloseStatus.POLICY_VIOLATION);
            return;
        }
        addToCurrentSession(socketSession, userDetails);
    }

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

    @Override
    public void afterConnectionClosed(WebSocketSession socketSession, CloseStatus status) {
        webSocketSessionListMap.remove(getSocketSessionId(socketSession));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        List<WebSocketSession> webSocketSessions = webSocketSessionListMap.get(getSocketSessionId(session)).webSocketSessions();
        for(WebSocketSession webSocketSession: webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }

    public String getSocketSessionId(WebSocketSession session) {
        String url = session.getUri().toString();
        String[] split = url.split("/");
        return split[split.length - 1];
    }
}
