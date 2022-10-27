package edu.uwp.appfactory.tow.controllers.policedepartment;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.PDUserRequest;
import edu.uwp.appfactory.tow.responseObjects.PDUAuthResponse;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class PDUserService {

    private final PDUserRepository pdUserRepository;
    private final PDAdminRepository pdAdminRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    @Autowired
    public PDUserService(PDUserRepository pdUserRepository, PDAdminRepository pdAdminRepository, UsersRepository usersRepository, JwtUtils jwtUtils, PasswordEncoder encoder) {
        this.pdUserRepository = pdUserRepository;
        this.pdAdminRepository = pdAdminRepository;
        this.usersRepository = usersRepository;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
    }

    /**
     * Retrieves the User using a UUID.
     * @param userId - UUID of user to retrieve
     * @return user if UUID is present in database, otherwise null
     */
    public PDUser get(UUID userId) {
        Optional<PDUser> user = pdUserRepository.findById(userId);
        return user.orElse(null);
    }

    //todo fix this bad code
    /**
     * Registers a new PD user.
     * @param pdUserRequest - PD user information to be added.
     * @param token - token to ensure request is authorized
     * @return information of new PD user if successful, else null
     */
    public ResponseEntity<PDUAuthResponse> register(PDUserRequest pdUserRequest, String token) {
        if(!AccountInformationValidator.validateEmail(pdUserRequest.getEmail())){
            throw new ResponseStatusException(BAD_REQUEST,"Typo in email");
        }
        if(!AccountInformationValidator.validateEmail(pdUserRequest.getPassword())){
            throw new ResponseStatusException(BAD_REQUEST,"Not secure password");
        }
        if (!usersRepository.existsByEmail(pdUserRequest.getEmail())) {
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
            return ResponseEntity.ok(new PDUAuthResponse(frontID,password));
        }
        throw new ResponseStatusException(BAD_REQUEST,"Email already exists");
    }

    /**
     * PATCH
     */


    /**
     * DELETE
     */

    /**
     * Generates a random 6 character length UUID.
     * @return UUID converted to a String
     */
    private String generatePDUserUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
