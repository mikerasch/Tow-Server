package edu.uwp.appfactory.tow.websockets;
import edu.uwp.appfactory.tow.websockets.handlers.HeartbeatWebSocketHandler;
import edu.uwp.appfactory.tow.websockets.handlers.TowJobWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    private final CustomHandshakeInterceptor customHandshakeInterceptor;
    private final HeartbeatWebSocketHandler heartbeatHandler;
    private final TowJobWebSocketHandler towJobWebSocketHandler;
    public WebSocketConfig(CustomHandshakeInterceptor customHandshakeInterceptor, HeartbeatWebSocketHandler heartbeatHandler, TowJobWebSocketHandler towJobWebSocketHandler) {
        this.customHandshakeInterceptor = customHandshakeInterceptor;
        this.heartbeatHandler = heartbeatHandler;
        this.towJobWebSocketHandler = towJobWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(heartbeatHandler, "/heartbeat")
                .addInterceptors(customHandshakeInterceptor);
        registry.addHandler(towJobWebSocketHandler, "/websocket/{userId}")
                .addInterceptors(customHandshakeInterceptor);
    }
}
