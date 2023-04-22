package edu.uwp.appfactory.tow.firebase;

import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseTokenDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.NotifyDriverDTO;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
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

    /**
     * Sends a notification message to a tow truck driver using Firebase Cloud Messaging (FCM) service.
     *
     * @param firebaseDTO A FirebaseDTO object containing the message to be sent and the recipient's device token.
     * @return A ResponseEntity object with a String response body indicating whether the message was sent successfully.
     */
    @PostMapping("/driver/message")
    public ResponseEntity<String> sendNotificationToTowTruck(@RequestBody FirebaseDTO firebaseDTO) {
        firebaseMessagingService.sendNotification(firebaseDTO);
        return ResponseEntity.ok("Message sent to Tow Truck");
    }

    /**
     * Stores a Firebase Cloud Messaging (FCM) token for a user.
     *
     * @param firebaseToken A FirebaseTokenDTO object containing the FCM token to be stored.
     * @param user The authenticated user for whom the FCM token is being stored.
     * @return A ResponseEntity object with a String response body indicating whether the FCM token was stored successfully.
     */
    @PostMapping("/store/token")
    public ResponseEntity<String> storeFirebaseToken(@RequestBody FirebaseTokenDTO firebaseToken, @AuthenticationPrincipal UserDetailsImpl user) {
        return firebaseMessagingService.storeToken(firebaseToken,user);
    }

    /**
     * Sends a notification message to a tow truck driver using Firebase Cloud Messaging (FCM) service.
     *
     * @param notifyDriverDTO A NotifyDriverDTO object containing the message to be sent and the recipient driver's FCM token.
     * @param user The authenticated user initiating the notification.
     * @return A ResponseEntity object with a String response body indicating whether the message was sent successfully.
     */
    @PostMapping("/towtruck/message")
    public ResponseEntity<String> sendNotificationToDriver(@RequestBody NotifyDriverDTO notifyDriverDTO, @AuthenticationPrincipal UserDetailsImpl user){
        firebaseMessagingService.sendNotificationToDriver(notifyDriverDTO,user);
        return ResponseEntity.ok("Message sent To Driver");
    }
}
