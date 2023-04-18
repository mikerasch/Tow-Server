package edu.uwp.appfactory.tow.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseTokenDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.NotifyDriverDTO;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;
    private final UsersRepository usersRepository;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging, UsersRepository usersRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.usersRepository = usersRepository;
    }

    /**
     * Sends a notification message to a device with the specified FCM token using the FirebaseMessaging service.
     *
     * @param firebaseDTO A FirebaseDTO object containing the title, body, and FCM token for the message to be sent.
     * @throws FirebaseMessagingException If an error occurs while attempting to send the message.
     */
    public void sendNotification(FirebaseDTO firebaseDTO) {
        String title = firebaseDTO.getTitle();
        String body = firebaseDTO.getBody();
        String token = firebaseDTO.getToken();

        Map<String,String> data = new HashMap<>();
        data.put("message_title",title);
        data.put("message_body",body);

        Message message = Message
                .builder()
                .setToken(token)
                .putAllData(data)
                .build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Error while trying to send a firebase message. This might be due to either an invalid token or the servers are down");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while sending firebase message");
        }
    }

    /**
     * Sends a notification to a device using Firebase Cloud Messaging (FCM).
     *
     * @param title the title of the notification.
     * @param token the FCM registration token of the device to send the notification to.
     * @param longitude the longitude of the device's location.
     * @param latitude the latitude of the device's location.
     * @param phone the phone number associated with the device.
     * @param firstName the first name of the user associated with the device.
     * @param lastName the last name of the user associated with the device.
     * @param firebaseId the Firebase ID of the user associated with the device.
     * @param channel the channel to send the notification to.
     * @throws FirebaseMessagingException if an error occurs while sending the message.
     */
    public void sendNotification(String title, String token, double longitude, double latitude, String phone, String firstName, String lastName, String firebaseId, String channel) throws FirebaseMessagingException {
        Map<String,String> data = new HashMap<>();
        data.put("title", title);
        data.put("longitude", String.valueOf(longitude));
        data.put("latitude", String.valueOf(latitude));
        data.put("phone", phone);
        data.put("name",firstName + " " + lastName);
        data.put("firebaseId",firebaseId);
        data.put("channel",channel);
        Message message = Message
                .builder()
                .setToken(token)
                .putAllData(data)
                .build();
        firebaseMessaging.send(message);
    }

    /**
     * Stores a Firebase Cloud Messaging (FCM) token for a user in the database.
     *
     * @param firebaseToken the FCM token to store.
     * @param users the current user details.
     */
    public ResponseEntity<String> storeToken(FirebaseTokenDTO firebaseToken, UserDetailsImpl users) {
        Optional<Users> optionalUser = usersRepository.findByEmail(users.getEmail());
        if(optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found, fire base token was not added");
        }
        Users user = optionalUser.get();
        user.setFbToken(firebaseToken.getFireToken());
        usersRepository.save(user);
        return ResponseEntity.ok("Firebase token added");
    }

    /**
     * Sends a notification to a tow truck driver using Firebase Cloud Messaging (FCM).
     *
     * @param driver the driver that needs towing.
     * @param tcUsersAvailable the list of tow truck users who are available to respond to the request.
     */
    public void sendNotificationToTowTruck(Drivers driver, List<TCUser> tcUsersAvailable) {
        if(tcUsersAvailable.isEmpty()){
            return;
        }
        TCUser tcUser = tcUsersAvailable.get(0);
        try {
            sendNotification("Request Incoming", tcUser.getFbToken(), driver.getLongitude(), driver.getLatitude(),tcUser.getPhone(), tcUser.getFirstname(), tcUser.getLastname(),tcUser.getFbToken(),"null");
            log.debug("Sending notification to firebase id: {}", tcUser.getFbToken());
        } catch (FirebaseMessagingException e) {
            log.warn("While sending push notification to tow truck, FirebaseMessagingException thrown: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sends a notification to a driver using Firebase Cloud Messaging (FCM).
     *
     * @param notifyDriverDTO the notification to send.
     * @param user the current user details.
     */
    public void sendNotificationToDriver(NotifyDriverDTO notifyDriverDTO, UserDetailsImpl user) {
        try {
            sendNotification(notifyDriverDTO.getTitle(),notifyDriverDTO.getToken(),Double.parseDouble(notifyDriverDTO.getLongitude()),Double.parseDouble(notifyDriverDTO.getLatitude()),user.getPhone(),user.getFirstname(),user.getLastname(),notifyDriverDTO.getToken(),notifyDriverDTO.getChannel());
        } catch (FirebaseMessagingException e){
            log.warn("While sending push notification to driver, FirebaseMessagingException thrown: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
