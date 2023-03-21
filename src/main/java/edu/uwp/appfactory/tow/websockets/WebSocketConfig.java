package edu.uwp.appfactory.tow.websockets;

import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    private final LocationService locationService;

    public WebSocketConfig(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService, LocationService locationService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.locationService = locationService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/heartbeat")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        HttpHeaders headers = request.getHeaders();
                        return headers.containsKey("Authorization");
                    }
                });
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new ServerWebSocketHandler(locationService, jwtUtils, userDetailsService);
    }
}
