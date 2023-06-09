package edu.uwp.appfactory.tow.controllers.password;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.password.ForgotPassRequest;
import edu.uwp.appfactory.tow.controllers.email.AsyncEmailService;
import edu.uwp.appfactory.tow.securityconfig.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Random;


@Service
@Slf4j
public class PasswordService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    private final AsyncEmailService sender;
    private final Random random;

    public PasswordService(UsersRepository usersRepository, PasswordEncoder encoder, AsyncEmailService sender) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
        this.sender = sender;
        random = new Random();
    }

    /**
     * Handles the event where a user forgets their password.
     *
     * @param forgotPassRequest - user information to send email to
     * @return - true if user exists, false otherwise
     */
    public boolean forgot(ForgotPassRequest forgotPassRequest) {
        Optional<Users> usersOptional = usersRepository.findByUsername(forgotPassRequest.getEmail());
        if(usersOptional.isEmpty()){
            log.warn("Upon a user requesting a new password reset, could not find User");
            return false;
        }
        Users user = usersOptional.get();
        int token = generateRandomToken();
        user.setResetToken(token);
        user.setResetDate(String.valueOf(LocalDate.now()));
        usersRepository.save(user);
        sender.submitEmailResetExecution(user, token);
        return true;
    }

    /**
     * Generates a random 6 digit token.
     *
     * @return 6 digit token
     */
    private int generateRandomToken() {
        return random.nextInt(900000) + 100000;
    }

    /**
     * Verifies if a link is still valid to be registered on.
     *
     * @param email - email to search the database
     * @param token - token to verify it is valid
     * @return true if link and user token is still valid, false otherwise
     */
    public boolean verify(String email, int token) {
        Optional<Users> usersOptional = usersRepository.findByUsername(email);
        if(usersOptional.isEmpty()){
            log.warn("Upon verifying email link is still valid, could not find user in database");
            return false;
        }
        Users user = usersOptional.get();
        LocalDate userResetDate = LocalDate.parse(user.getResetDate());
        Period periodBetween = Period.between(userResetDate, LocalDate.now());
        return periodBetween.getDays() < 8 && user.getResetToken() == token;
    }

    /**
     * Handles resetting a user's password.
     * The user must provide a valid email, token, and password.
     *
     * @param email - email to find account
     * @param token - token to verify session is valid
     * @param password - password verification to ensure user owns account
     * @return - true if user could be found and verified, false otherwise
     */
    public boolean reset(String email, int token, String password) {
        Optional<Users> usersOptional = usersRepository.findByUsername(email);
        if(usersOptional.isEmpty()){
            log.warn("Up resetting user's password, could not find user in database");
            return false;
        }
        Users user = usersOptional.get();
        LocalDate userResetDate = LocalDate.parse(user.getResetDate());
        Period periodBetween = Period.between(userResetDate, LocalDate.now());
        if(periodBetween.getDays() < 8 && user.getResetToken().equals(token)){
            user.setPassword(encoder.encode(password));
            usersRepository.save(user);
            return true;
        }
        return false;
    }
}
