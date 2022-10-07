package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.PDUserRequest;
import edu.uwp.appfactory.tow.responseObjects.PDUAuthResponse;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@Service
public class PDUserService {

    private final PDUserRepository pdUserRepository;
    private final PDAdminRepository pdAdminRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    private final AsyncEmailService sendEmail;
    private final JwtUtils jwtUtils;


    @Autowired
    public PDUserService(PDUserRepository pdUserRepository, PDAdminRepository pdAdminRepository, UsersRepository usersRepository, AsyncEmailService sendEmail, JwtUtils jwtUtils, PasswordEncoder encoder) {
        this.pdUserRepository = pdUserRepository;
        this.pdAdminRepository = pdAdminRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
    }

    /**
     * GET
     */
    public PDUser get(UUID userId) {
        Optional<PDUser> user = pdUserRepository.findById(userId);
        return user.orElse(null);
    }


    /**
     * POST
     */
    public PDUAuthResponse register(PDUserRequest pdUserRequest, String token) {
        if (usersRepository.existsByEmail(pdUserRequest.getEmail())) {
            UUID adminUUID = UUID.fromString(jwtUtils.getUUIDFromJwtToken(token));

            String frontID = "";
            String password = generatePDUserUUID();

            Optional<PDAdmin> adminsOptional = pdAdminRepository.findById(adminUUID);
            if (adminsOptional.isPresent()) {
                PDAdmin admin = adminsOptional.get();
                frontID = admin.getDepartmentShort() + "-" + generatePDUserUUID();
            }

            PDUser pdUser = new PDUser(
                    pdUserRequest.getEmail(),
                    pdUserRequest.getEmail(),
                    encoder.encode(pdUserRequest.getPassword()),
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
            return new PDUAuthResponse(frontID, password);
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
