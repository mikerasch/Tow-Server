package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import edu.uwp.appfactory.tow.requestObjects.TCUserRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.services.AsyncEmail;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Controller
public class TCUserController {

    private final TCUserRepository tcUserRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmail sendEmail;
    private final PasswordEncoder encoder;


    @Autowired
    public TCUserController(TCUserRepository tcUserRepository, UsersRepository usersRepository, AsyncEmail sendEmail, PasswordEncoder encoder) {
        this.tcUserRepository = tcUserRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
    }

    /**
     * GET
     */
    public TCUser get(UUID userId) {
        Optional<TCUser> user = tcUserRepository.findById(userId);
        return user.orElse(null);
    }

    /**
     * GET ALL
     */
    public List<TCUser> getAll(UUID adminUUID) {
        return tcUserRepository.findAllByAdminUUID(adminUUID);
    }


    /**
     * POST
     */
    public ResponseEntity<?> register(TCUserRequest tcUserRequest, UUID adminUUID) {
        if (!usersRepository.existsByEmail(tcUserRequest.getEmail())) {
            TCUser tcuser = new TCUser(tcUserRequest.getEmail(),
                    tcUserRequest.getEmail(),
                    encoder.encode(tcUserRequest.getPassword()),
                    tcUserRequest.getFirstname(),
                    tcUserRequest.getLastname(),
                    tcUserRequest.getPhone(),
                    ERole.ROLE_TCUSER.name(),
                    0.0f,
                    0.0f,
                    false,
                    adminUUID);

            tcuser.setVerifyToken(generateEmailUUID());
            tcuser.setVerifyDate(String.valueOf(LocalDate.now()));
            tcuser.setVerEnabled(false);
            usersRepository.save(tcuser);
            sendEmail.sendEmailAsync(tcuser);
            TestVerifyResponse x = new TestVerifyResponse(tcuser.getVerifyToken());
            return ResponseEntity.ok(x);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * PATCH
     */


    /**
     * DELETE
     */


    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
