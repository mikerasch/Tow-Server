package edu.uwp.appfactory.tow.websocket;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.UUID;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/chat", "/queue");
        registry.setApplicationDestinationPrefixes("/app", "/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String jwtToken = accessor.getFirstNativeHeader("Authorization");

                    String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);

                    UserDetails userDetails = userDetailsService.loadUserByUUID(UUID.fromString(userId));

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());


//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    String jwt = jwtUtils.generateJwtToken(authentication);
//                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//                    Optional<Users> usersOptional = usersRepository.findByUsername(loginRequest.getEmail());
//                    // bullshit
//                    Jwt jwt = jwtDecoder.decode(accessToken);
//                    Authentication authentication = converter.convert(jwt);


                    accessor.setUser((Principal) authentication.getPrincipal());
                }
                return message;
            }
        });
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
