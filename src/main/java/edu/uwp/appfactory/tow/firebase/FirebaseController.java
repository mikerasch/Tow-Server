package edu.uwp.appfactory.tow.firebase;

import com.google.firebase.messaging.FirebaseMessagingException;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseTokenDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.NotifyDriverDTO;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firebase")
public class FirebaseController {
    private final FirebaseMessagingService firebaseMessagingService;
    public FirebaseController(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;

    }

    // todo don't throw exception from controller
    @PostMapping("/driver/message")
    public ResponseEntity<String> sendNotificationToTowTruck(@RequestBody FirebaseDTO firebaseDTO) throws FirebaseMessagingException {
        firebaseMessagingService.sendNotification(firebaseDTO);
        return ResponseEntity.ok("Message sent");
    }

    @PostMapping("/store/token")
    public ResponseEntity<String> storeFirebaseToken(@RequestBody FirebaseTokenDTO firebaseToken, @AuthenticationPrincipal UserDetailsImpl user) {
        firebaseMessagingService.storeToken(firebaseToken,user);
        return ResponseEntity.ok("Firebase token added");
    }
    @PostMapping("/towtruck/message")
    public ResponseEntity<String> sendNotificationToDriver(@RequestBody NotifyDriverDTO notifyDriverDTO, @AuthenticationPrincipal UserDetailsImpl user){
        System.out.println("HELLO: " + notifyDriverDTO.getChannel());
        firebaseMessagingService.sendNotificationToDriver(notifyDriverDTO,user);
        return ResponseEntity.ok("Message sent");
    }
}
