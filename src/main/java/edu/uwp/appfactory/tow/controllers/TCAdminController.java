package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCAdminRepository;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import edu.uwp.appfactory.tow.requestObjects.TCAdminRequest;
import edu.uwp.appfactory.tow.requestObjects.TCUserRequest;
import edu.uwp.appfactory.tow.requestObjects.VerifyRequest;
import edu.uwp.appfactory.tow.services.AsyncEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.UUID;


@Controller
public class TCAdminController {

    private final TCAdminRepository tcAdminRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmail sendEmail;
    private final PasswordEncoder encoder;


    @Autowired
    public TCAdminController(TCAdminRepository tcAdminRepository, UsersRepository usersRepository, AsyncEmail sendEmail, PasswordEncoder encoder) {
        this.tcAdminRepository = tcAdminRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
    }

    /**
     * GET
     */


    /**
     * POST
     */
    public ResponseEntity<?> register(TCAdminRequest tcAdminRequest) {
        if (!usersRepository.existsByEmail(tcAdminRequest.getEmail())) {
            TCAdmin tcAdmin = new TCAdmin(tcAdminRequest.getEmail(),
                    tcAdminRequest.getEmail(),
                    encoder.encode(tcAdminRequest.getPassword()),
                    tcAdminRequest.getFirstname(),
                    tcAdminRequest.getLastname(),
                    tcAdminRequest.getPhone(),
                    ERole.ROLE_TCADMIN.name(),
                    tcAdminRequest.getCompany());


            tcAdmin.setVerifyToken(generateEmailUUID());

            tcAdmin.setVerifyDate(String.valueOf(LocalDate.now()));
            tcAdmin.setVerEnabled(false);
            usersRepository.save(tcAdmin);
            sendEmail.sendEmailAsync(tcAdmin);
            VerifyRequest x = new VerifyRequest(tcAdmin.getVerifyToken());
            return ResponseEntity.ok(x);
        } else {
            return ResponseEntity.status(400).build(); //TODO:Not sure if .build is correct
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
