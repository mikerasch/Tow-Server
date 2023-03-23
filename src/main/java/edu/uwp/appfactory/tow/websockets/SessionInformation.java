package edu.uwp.appfactory.tow.websockets;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public record SessionInformation(List<WebSocketSession> webSocketSessions, List<UserDetails> userDetails) {
}
