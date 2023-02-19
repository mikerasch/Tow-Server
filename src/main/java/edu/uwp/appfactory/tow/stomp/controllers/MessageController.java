package edu.uwp.appfactory.tow.stomp.controllers;

import edu.uwp.appfactory.tow.stomp.models.Message;
import edu.uwp.appfactory.tow.stomp.models.MessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {
    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public MessageResponse getPrivateMessage(Message message, final Principal principal) {
        return new MessageResponse(message.getContent());
    }
}