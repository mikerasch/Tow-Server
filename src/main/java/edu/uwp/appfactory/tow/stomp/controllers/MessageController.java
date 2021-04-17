package edu.uwp.appfactory.tow.stomp.controllers;
import edu.uwp.appfactory.tow.controllers.LocationController;
import edu.uwp.appfactory.tow.requestObjects.TCULocationRequest;
import edu.uwp.appfactory.tow.requestObjects.TCUserRequest;
import edu.uwp.appfactory.tow.stomp.models.MessageRequest;
import edu.uwp.appfactory.tow.stomp.models.MessageResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.List;


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

    @MessageMapping("/greetings")
    @SendToUser("/queue/greetings")
    public String reply(@Payload String message,
                        Principal user) {
        System.out.println(message);
        System.out.println(user );
        return user.toString();
    }

    @PreAuthorize("hasRole('TCUSER')")
    @MessageMapping("/my-location")
    //@SendTo("/topic/setlocation")
    public ResponseEntity<?> setlocation(TCULocationRequest setRequest) throws Exception {
        String userUUID = jwtUtils.getUUIDFromJwtToken(setRequest.getJwtToken());
        System.out.println("lat: " + setRequest.getLatitude() + " long: " + setRequest.getLongitude() + " active: " + setRequest.isActive());
        return locationController.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
                ? ResponseEntity.status(204).body(null)
                : ResponseEntity.status(400).build();
    }



    @PreAuthorize("hasRole('PDUSER')")
    @MessageMapping("/driver-locations")
    public ResponseEntity<?> getLocations(@RequestBody TCUserRequest driversRequest) {
        System.out.println("lat: " + driversRequest.getLatitude() + " long: " + driversRequest.getLongitude() + " Radius: " + driversRequest.getRadius());

        List<?> data = locationController.findByDistance(driversRequest.getLatitude(), driversRequest.getLongitude(), driversRequest.getRadius());

        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(400).build();
        }
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
        return new MessageResponse(exception.getMessage());
    }


}
