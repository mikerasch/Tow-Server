package edu.uwp.appfactory.tow.stomp.controllers;

import edu.uwp.appfactory.tow.stomp.models.MessageRequest;
import edu.uwp.appfactory.tow.stomp.models.MessageResponse;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

    @MessageMapping("/guestchat")  // here
    @SendTo("/topic/guestchats")  // here
    public MessageResponse handleMessaging(MessageRequest message) throws Exception {
        Thread.sleep(1000); // simulated delay for actual server behavior
        return new MessageResponse(HtmlUtils.htmlEscape(message.getMessage()));
    }

    @MessageMapping("/guestupdate")
    @SendTo("/topic/guestupdates")
    public MessageResponse handleUserTyping(MessageRequest message) throws Exception {
        return new MessageResponse("Someone is typing...");
    }

    @MessageMapping("/guestjoin")
    @SendTo("/topic/guestnames")
    public MessageResponse handleMemberJoins(String string) throws Exception {
        System.out.println(string);
        return new MessageResponse(string);
    }

    @MessageExceptionHandler
    @SendTo("/topic/errors")
    public MessageResponse handleExcpetion(Throwable exception) {
        return new MessageResponse("An error happened.");
    }
}
