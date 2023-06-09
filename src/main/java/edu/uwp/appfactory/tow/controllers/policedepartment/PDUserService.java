package edu.uwp.appfactory.tow.controllers.policedepartment;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.PDUserRequest;
import edu.uwp.appfactory.tow.responseobjects.PDUAuthResponse;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.securityconfig.models.ERole;
import edu.uwp.appfactory.tow.securityconfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
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
    public PDUserService(PDUserRepository pdUserRepository, PDAdminRepository pdAdminRepository, UsersRepository usersRepository, PasswordEncoder encoder) {
        this.pdUserRepository = pdUserRepository;
        this.pdAdminRepository = pdAdminRepository;
        this.usersRepository = usersRepository;
        this.encoder = encoder;
    }

    /**
     * Retrieves the User using a UUID.
     *
     * @return user if UUID is present in database, otherwise null
     */
    public PDUser get(UserDetailsImpl userDetails) {
        return pdUserRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
    }

    /**
     * Registers a new PD user.
     *
     * @param pdUserRequest - PD user information to be added.
     * @return information of new PD user if successful, else null
     */
    public ResponseEntity<PDUAuthResponse> register(PDUserRequest pdUserRequest, UserDetailsImpl userDetails) {
        Users users = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
        if(!AccountInformationValidator.validateEmail(pdUserRequest.getEmail())){
            throw new ResponseStatusException(BAD_REQUEST,"Typo in email");
        }

        if(!AccountInformationValidator.validatePassword(pdUserRequest.getPassword()).isEmpty()){
            throw new ResponseStatusException(BAD_REQUEST,"Not secure password");
        }
        if(usersRepository.existsByEmail(pdUserRequest.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "Email already exists");
        }

        long adminId = users.getId();
        String frontID = "";
        String password = generatePDUserUUID();

        Optional<PDAdmin> adminsOptional = pdAdminRepository.findById(adminId);
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
                adminId
        );
        pdUser.setVerifyToken("");
        pdUser.setVerifyDate(String.valueOf(LocalDate.now()));
        pdUser.setVerEnabled(true);
        pdUserRepository.save(pdUser);
        return ResponseEntity.ok(new PDUAuthResponse(frontID, password));
    }

    /**
     * Generates a random 6 character length UUID.
     *
     * @return UUID converted to a String
     */
    private String generatePDUserUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
