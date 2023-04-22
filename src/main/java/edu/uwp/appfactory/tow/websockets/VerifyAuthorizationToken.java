package edu.uwp.appfactory.tow.websockets;

import edu.uwp.appfactory.tow.securityconfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Configuration
public class VerifyAuthorizationToken {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public VerifyAuthorizationToken(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }
    public UserDetails verifyAuthorizationToken(WebSocketSession socketSession) throws IOException {
        String authToken = socketSession.getHandshakeHeaders().getFirst("Authorization");
        String userUUID = jwtUtils.getUUIDFromJwtToken(authToken);
        if(userUUID == null) {
            socketSession.close();
            return null;
        }
        return userDetailsService.loadUserByUUID(Long.valueOf(userUUID));
    }
}
