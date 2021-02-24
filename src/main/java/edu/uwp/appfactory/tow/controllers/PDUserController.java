package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import edu.uwp.appfactory.tow.requestObjects.PDUAuthRequest;
import edu.uwp.appfactory.tow.requestObjects.PDUserRequest;
import edu.uwp.appfactory.tow.services.AsyncEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@Controller
public class PDUserController {

    private final PDUserRepository pdUserRepository;
    private final PDAdminRepository pdAdminRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmail sendEmail;
    private final PasswordEncoder encoder;


    @Autowired
    public PDUserController(PDUserRepository pdUserRepository, PDAdminRepository pdAdminRepository, UsersRepository usersRepository, AsyncEmail sendEmail, PasswordEncoder encoder) {
        this.pdUserRepository = pdUserRepository;
        this.pdAdminRepository = pdAdminRepository;
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
    public PDUAuthRequest register(PDUserRequest pdUserRequest, UUID adminUUID) {
        if (!usersRepository.existsByEmail(pdUserRequest.getEmail())) {

            String frontID = "";
            String password = generatePDUserUUID();

            Optional<PDAdmin> adminsOptional = pdAdminRepository.findById(adminUUID);
            if (adminsOptional.isPresent()) {
                PDAdmin admin = adminsOptional.get();
                frontID = admin.getDepartmentShort() + "-" + generatePDUserUUID();
            }

            PDUser pdUser = new PDUser(pdUserRequest.getEmail(),
                    frontID,
                    encoder.encode(password),

                    pdUserRequest.getFirstname(),
                    pdUserRequest.getLastname(),
                    "",
                    ERole.ROLE_PDUSER.name(),
                    frontID,
                    adminUUID);

            pdUser.setVerifyToken("");
            pdUser.setVerifyDate(String.valueOf(LocalDate.now()));
            pdUser.setVerEnabled(true);
            usersRepository.save(pdUser);
            PDUAuthRequest x = new PDUAuthRequest(frontID, password);
            return x;
        } else {
            return null;
        }
    }

    /**
     * PATCH
     */


    /**
     * DELETE
     */

    private String generatePDUserUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
