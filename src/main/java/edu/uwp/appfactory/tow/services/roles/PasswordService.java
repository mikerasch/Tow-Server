package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestObjects.password.ForgotPassRequest;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Random;


@Service
public class PasswordService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    private final AsyncEmailService sender;

    @Autowired
    public PasswordService(UsersRepository usersRepository, PasswordEncoder encoder, AsyncEmailService sender) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
        this.sender = sender;
    }

    /**
     * Handles the event where a user forgets their password.
     * @param forgotPassRequest - user information to send email to
     * @return - true if user exists, false otherwise
     */
    public boolean forgot(ForgotPassRequest forgotPassRequest) {
        System.out.println(forgotPassRequest.getEmail());
        Optional<Users> usersOptional = usersRepository.findByUsername(forgotPassRequest.getEmail());
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();

            // generate random 6 digit token
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            String tokenString = String.format("%06d", number);
            int token = Integer.parseInt(tokenString);

            user.setResetToken(token);
            user.setResetDate(String.valueOf(LocalDate.now()));
            usersRepository.save(user);
            sender.sendResetEmailAsync(user, token);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies if a link is still valid to be registered on.
     * @param email - email to search the database
     * @param token - token to verify it is valid
     * @return true if link and user token is still valid, false otherwise
     */
    public boolean verify(String email, int token) {
        Optional<Users> usersOptional = usersRepository.findByUsername(email);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            LocalDate userResetDate = LocalDate.parse(user.getResetDate());
            Period periodBetween = Period.between(userResetDate, LocalDate.now());
            return periodBetween.getDays() < 8 && user.getResetToken() == token;
        } else {
            return false;
        }
    }

    /**
     * Handles resetting a user's password.
     * The user must provide a valid email, token, and password.
     * @param email - email to find account
     * @param token - token to verify session is valid
     * @param password - password verification to ensure user owns account
     * @return - true if user could be found and verified, false otherwise
     */
    public boolean reset(String email, int token, String password) {
        Optional<Users> usersOptional = usersRepository.findByUsername(email);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            LocalDate userResetDate = LocalDate.parse(user.getResetDate());
            Period periodBetween = Period.between(userResetDate, LocalDate.now());

            if (periodBetween.getDays() < 8 && user.getResetToken() == token) {
                user.setPassword(encoder.encode(password));
                usersRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
