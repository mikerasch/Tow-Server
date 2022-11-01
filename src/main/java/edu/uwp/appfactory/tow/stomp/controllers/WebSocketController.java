package edu.uwp.appfactory.tow.stomp.controllers;

import edu.uwp.appfactory.tow.stomp.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatting")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/send-private-message/{uuid}")
    public void sendPrivateMessage(@PathVariable("uuid") final String uuid, @RequestBody final Message message){
        webSocketService.notifyPrivate(uuid,message.getContent());
    }
}
