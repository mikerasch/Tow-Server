package edu.uwp.appfactory.tow.stomp.controllers;

import edu.uwp.appfactory.tow.controllers.LocationController;
import edu.uwp.appfactory.tow.stomp.models.MessageRequest;
import edu.uwp.appfactory.tow.stomp.models.MessageResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import edu.uwp.appfactory.tow.controllers.LocationController;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;

@Controller
public class MessageController {
    private final LocationController locationController;
    private final JwtUtils jwtUtils;

    public MessageController(LocationController locationController, JwtUtils jwtUtils) {
        this.locationController = locationController;
        this.jwtUtils = jwtUtils;
    }

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

    @MessageMapping("/driverconnect")
    @SendTo("/topic/driverconnected")
    public MessageResponse handleconnectionrequest(MessageRequest message) throws Exception {

        return new MessageResponse("Driver connecting....");
    }

    @PreAuthorize("hasRole('TCUSER')")
    @MessageMapping("//my-location")
    @SendTo("/topic/driverconnected")
    public MessageResponse setlocation(MessageRequest message) throws Exception {

        System.out.println(message);
        return new MessageResponse("Setting location....");
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
