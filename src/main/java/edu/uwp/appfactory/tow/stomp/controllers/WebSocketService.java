package edu.uwp.appfactory.tow.stomp.controllers;

import edu.uwp.appfactory.tow.stomp.models.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void notifyPrivate(final String id, final String message){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(id);
        headerAccessor.setLeaveMutable(true);
        MessageResponse messageResponse = new MessageResponse(message);
        simpMessagingTemplate.convertAndSendToUser(id,"/topic/private-messages",messageResponse,headerAccessor.getMessageHeaders());
    }
}
