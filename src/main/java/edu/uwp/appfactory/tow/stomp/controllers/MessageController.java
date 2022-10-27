package edu.uwp.appfactory.tow.stomp.controllers;
import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.requestObjects.location.TCULocationRequest;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.TCUserRequest;
import edu.uwp.appfactory.tow.stomp.models.MessageRequest;
import edu.uwp.appfactory.tow.stomp.models.MessageResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
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


    private final LocationService locationService;

    private final JwtUtils jwtUtils;

    public MessageController(LocationService locationService, JwtUtils jwtUtils) {
        this.locationService = locationService;
        this.jwtUtils = jwtUtils;
    }

    @MessageMapping("/guestchat")  // here
    @SendTo("/topic/guestchats")  // here
    public MessageResponse handleMessaging(MessageRequest message) throws InterruptedException {
        Thread.sleep(1000); // simulated delay for actual server behavior
        return new MessageResponse(HtmlUtils.htmlEscape(message.getMessage()));
    }

    @MessageMapping("/guestupdate")
    @SendTo("/topic/guestupdates")
    public MessageResponse handleUserTyping(MessageRequest message){
        return new MessageResponse("Someone is typing...");
    }

    @MessageMapping("/driverconnect")
    @SendTo("/topic/driverconnected")
    public MessageResponse handleConnectionRequest(MessageRequest message){
        return new MessageResponse("Driver connecting....");
    }

    @MessageMapping("/greetings")
    @SendToUser("/queue/greetings")
    public String reply(@Payload String message,
                        Principal user) {
        return user.toString();
    }

    @PreAuthorize("hasRole('TCUSER')")
    @MessageMapping("/my-location")
    //@SendTo("/topic/setlocation")
    public ResponseEntity<?> setlocation(TCULocationRequest setRequest){
        String userUUID = jwtUtils.getUUIDFromJwtToken(setRequest.getJwtToken());
        return locationService.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
                ? ResponseEntity.status(204).body(null)
                : ResponseEntity.status(400).build();
    }



    @PreAuthorize("hasRole('PDUSER')")
    @MessageMapping("/driver-locations")
    public ResponseEntity<?> getLocations(@RequestBody TCUserRequest driversRequest) {
        List<?> data = locationService.findByDistance(driversRequest.getLatitude(), driversRequest.getLongitude(), driversRequest.getRadius());
        if(data.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(data);
    }



    @MessageMapping("/guestjoin")
    @SendTo("/topic/guestnames")
    public MessageResponse handleMemberJoins(String string){
        return new MessageResponse(string);
    }

    @MessageExceptionHandler
    @SendTo("/topic/errors")
    public MessageResponse handleExcpetion(Throwable exception) {
        return new MessageResponse(exception.getMessage());
    }


}
