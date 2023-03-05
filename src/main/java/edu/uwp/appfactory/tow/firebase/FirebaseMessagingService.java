package edu.uwp.appfactory.tow.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseDTO;
import edu.uwp.appfactory.tow.requestobjects.firebase.FirebaseTokenDTO;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;
    private final UsersRepository usersRepository;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging, UsersRepository usersRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.usersRepository = usersRepository;
    }

    public void sendNotification(FirebaseDTO firebaseDTO) throws FirebaseMessagingException {
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

        firebaseMessaging.send(message);
    }
    public void sendNotification(String title, String token, float longitude, float latitude, String phone) throws FirebaseMessagingException {
        Map<String,String> data = new HashMap<>();
        data.put("title", title);
        data.put("longitude", String.valueOf(longitude));
        data.put("latitude", String.valueOf(latitude));
        data.put("phone", phone);
        Message message = Message
                .builder()
                .setToken(token)
                .putAllData(data)
                .build();

        firebaseMessaging.send(message);
    }

    // todo actually return something here
    public void storeToken(FirebaseTokenDTO firebaseToken, UserDetailsImpl users) {
        Optional<Users> optionalUser = usersRepository.findByEmail(users.getEmail());
        if(optionalUser.isEmpty()) {
            return;
        }
        Users user = optionalUser.get();
        user.setFbToken(firebaseToken.getFireToken());
        usersRepository.save(user);
    }

    public void sendNotificationToTowTruck(Drivers driver, List<TCUser> tcUsersAvailable) {
        if(tcUsersAvailable.isEmpty()){
            return;
        }
        TCUser tcUser = tcUsersAvailable.get(0);
        try{
            sendNotification("Request Incoming",tcUser.getFbToken(), driver.getLongitude(), driver.getLatitude(),driver.getPhone());
            // todo add some error handling
        } catch (FirebaseMessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
