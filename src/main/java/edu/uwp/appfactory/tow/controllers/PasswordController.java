package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.RoleRepository;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@EnableAutoConfiguration
@Controller
public class PasswordController {

    private final AuthenticationManager authenticationManager;

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final PasswordResetService sender;

    @Autowired
    public PasswordController(AuthenticationManager authenticationManager, UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, PasswordResetService sender) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.sender = sender;
    }
    public ResponseEntity<?> forgot(String email) {
        try {
            Optional<Users> usersOptional = usersRepository.findByUsername(email);
            if (usersOptional.isEmpty()) {
                return ResponseEntity
                        .status(500)
                        .body(new MessageResponse("Not successful!"));
            }
            Users user = usersOptional.get();

            // generate random 6 digit token
            Random rnd = new Random();
            int number = rnd.nextInt(999999);
            String tokenString = String.format("%06d", number);
            int token = Integer.parseInt(tokenString);

            // update the token value in the db for the user
            // int count = usersRepository.updateResetToken(token, email, dateTime);

            user.setResetToken(token);
            user.setResetDate(String.valueOf(LocalDate.now()));
            // user.set_date
            usersRepository.save(user);

            boolean didSend = sender.sendResetMail(user, token);

            if (didSend) {
                return ResponseEntity
                        .status(200)
                        .body(new MessageResponse("Successful!"));
            } else {
                return ResponseEntity
                        .status(500)
                        .body(new MessageResponse("Not successful!"));
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(498).body("Invalid Entries: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(499).body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> verify(String email, int token) {
        try {
            Optional<Users> usersOptional = usersRepository.findByUsername(email);
            if (usersOptional.isEmpty()) {
                return ResponseEntity
                        .status(500)
                        .body(new MessageResponse("Not successful!"));
            }
            Users user = usersOptional.get();
            LocalDate userResetDate = LocalDate.parse(user.getResetDate());
            Period periodBetween = Period.between(userResetDate, LocalDate.now());

            if (periodBetween.getDays() < 8) {
                if (user.getResetToken() == token) {
                    return ResponseEntity
                            .status(200)
                            .body(new MessageResponse("Successful!"));
                } else {
                    return ResponseEntity
                            .status(500)
                            .body(new MessageResponse("Not successful!"));
                }
            } else {
                return ResponseEntity
                        .status(500)
                        .body(new MessageResponse("Not successful!"));
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(498).body("Invalid Entries: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(499).body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> reset(String email, int token, String password) {
        try {

            Optional<Users> usersOptional = usersRepository.findByUsername(email);
            if (usersOptional.isEmpty()) {
                return ResponseEntity
                        .status(500)
                        .body(new MessageResponse("Not successful!"));
            }
            Users user = usersOptional.get();
            user.setPassword(encoder.encode(password));
//            user.setResetToken(null);
            usersRepository.save(user);
            return ResponseEntity
                    .status(500)
                    .body(new MessageResponse("Not successful!"));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(498).body("Invalid Entries: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(499).body("Error: " + e.getMessage());
        }
    }
}

//    public static void main (String []args) {
//        LocalDate resetDate = LocalDate.of(2020, 10, 24);
//
//        Period periodBetween = Period.between(resetDate, LocalDate.now());
//        System.out.println(String.valueOf(LocalDate.now()));
//        System.out.println(periodBetween.getDays());
//        if (periodBetween.getDays() < 8) {
//            System.out.println("valid");
//        } else {
//            System.out.println("invalid");
//        }
//    }
//}

//    LocalDateTime localNow = LocalDateTime.now();
//    ZonedDateTime myDateObj = localNow.atZone(ZoneId.of("UTC"));
//    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
//    String formattedDate = myDateObj.format(myFormatObj);
//        System.out.println(formattedDate);
