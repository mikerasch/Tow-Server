package edu.uwp.appfactory.tow.controllers.towcompany;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.TCUserRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.controllers.email.AsyncEmailService;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class TCUserService {

    private final TCUserRepository tcUserRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmailService sendEmail;
    private final PasswordEncoder encoder;

    public TCUserService(TCUserRepository tcUserRepository, UsersRepository usersRepository, AsyncEmailService sendEmail, PasswordEncoder encoder) {
        this.tcUserRepository = tcUserRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
    }

    /**
     * Retrieves the TCUser using a UUID.
     * @param userId - UUID of TCUser to retrieve.
     * @return user if UUID is present in database, otherwise null
     */
    public TCUser get(UUID userId) {
        Optional<TCUser> user = tcUserRepository.findById(userId);
        return user.orElse(null);
    }

    //todo look into what this does and why it's called findAllAdmin in a USER service
    public List<TCUser> getAll(UUID adminUUID) {
        return tcUserRepository.findAllByAdminUUID(adminUUID);
    }

    /**
     * Registers a new TCUser.
     * @param tcUserRequest - TCUser information to create a new account
     * @param adminUUID - AdminUUID to map to the user it will create.
     * @return token of newly created account if successful, otherwise 400 error
     */
    public ResponseEntity<TestVerifyResponse> register(TCUserRequest tcUserRequest, UUID adminUUID) {
        if(!AccountInformationValidator.validateEmail(tcUserRequest.getEmail())){
            throw new ResponseStatusException(BAD_REQUEST,"Typo in email");
        }
        List<String> errorPassword = AccountInformationValidator.validatePassword(tcUserRequest.getPassword());
        if(!errorPassword.isEmpty()){
            throw new ResponseStatusException(BAD_REQUEST, errorPassword.toString());
        }
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
            sendEmail.submitSignupEmailExecution(tcuser);
            TestVerifyResponse x = new TestVerifyResponse(tcuser.getVerifyToken());
            return ResponseEntity.ok(x);
        }
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    /**
     * Generates a random 6 character length UUID.
     * @return UUID converted to a String
     */
    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //todo add Patch and Delete
}