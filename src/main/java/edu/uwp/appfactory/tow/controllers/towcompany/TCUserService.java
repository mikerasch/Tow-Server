package edu.uwp.appfactory.tow.controllers.towcompany;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCAdminRepository;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.TCUserRequest;
import edu.uwp.appfactory.tow.responseobjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.controllers.email.AsyncEmailService;
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
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class TCUserService {

    private final TCUserRepository tcUserRepository;
    private final UsersRepository usersRepository;
    private final TCAdminRepository tcAdminRepository;
    private final AsyncEmailService sendEmail;
    private final PasswordEncoder encoder;

    public TCUserService(TCUserRepository tcUserRepository, UsersRepository usersRepository, TCAdminRepository tcadminRepository, AsyncEmailService sendEmail, PasswordEncoder encoder) {
        this.tcUserRepository = tcUserRepository;
        this.usersRepository = usersRepository;
        this.tcAdminRepository = tcadminRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
    }

    /**
     * Retrieves the TCUser using a UUID.
     * @return user if UUID is present in database, otherwise null
     */
    public TCUser get(UserDetailsImpl userDetails) {
        return tcUserRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
    }
    
    public List<TCUser> getAll(UserDetailsImpl userDetails) {
        return tcUserRepository.findAllByTcAdminId(userDetails.getId());
    }

    /**
     * Registers a new TCUser.
     * @param tcUserRequest - TCUser information to create a new account
     * @return token of newly created account if successful, otherwise 400 error
     */
    public ResponseEntity<TestVerifyResponse> register(TCUserRequest tcUserRequest, UserDetailsImpl userDetails) {
        TCAdmin user = tcAdminRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not finder user!"));
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
                    user
            );
            tcuser.setVerifyToken(generateEmailUUID());
            tcuser.setVerifyDate(String.valueOf(LocalDate.now()));
            tcuser.setVerEnabled(false);
            tcUserRepository.save(tcuser);
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
}
