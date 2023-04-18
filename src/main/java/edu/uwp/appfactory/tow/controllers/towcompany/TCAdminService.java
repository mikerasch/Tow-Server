package edu.uwp.appfactory.tow.controllers.towcompany;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.mappers.TCMapper;
import edu.uwp.appfactory.tow.repositories.TCAdminRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.TCAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TCAdminResponse;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.controllers.email.AsyncEmailService;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
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
public class TCAdminService {

    private final TCAdminRepository tcAdminRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmailService sendEmail;
    private final PasswordEncoder encoder;
    private final TCMapper tcMapper;

    public TCAdminService(TCAdminRepository tcAdminRepository, UsersRepository usersRepository, AsyncEmailService sendEmail, PasswordEncoder encoder, TCMapper tcMapper) {
        this.tcAdminRepository = tcAdminRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
        this.tcMapper = tcMapper;
    }

    /**
     * Retrieves the TCAdmin using a UUID.
     * @return user if UUID is present in database, otherwise null
     */
    public TCAdminResponse get(UserDetailsImpl userDetails) {
        Optional<TCAdmin> user = Optional.of(tcAdminRepository.findById(userDetails.getId()).orElseThrow());
        return user.map(tcMapper::map).orElse(null);
    }

    /**
     * Registers a new TCAdmin.
     * @param tcAdminRequest - TCAdmin information to create a new account
     * @return token response of newly created account, otherwise 400 error
     */
    public ResponseEntity<TestVerifyResponse> register(TCAdminRequest tcAdminRequest) {
        List<String> passwordViolations = AccountInformationValidator.validatePassword(tcAdminRequest.getPassword());
        if(!AccountInformationValidator.validateEmail(tcAdminRequest.getEmail())){
            throw new ResponseStatusException(BAD_REQUEST,"Bad email");
        }
        if(!passwordViolations.isEmpty()){
            throw new ResponseStatusException(BAD_REQUEST,passwordViolations.toString());
        }
        if (!usersRepository.existsByEmail(tcAdminRequest.getEmail())) {
            TCAdmin tcAdmin = new TCAdmin(tcAdminRequest.getEmail(),
                    tcAdminRequest.getEmail(),
                    encoder.encode(tcAdminRequest.getPassword()),
                    tcAdminRequest.getFirstname(),
                    tcAdminRequest.getLastname(),
                    tcAdminRequest.getPhone(),
                    ERole.ROLE_TCADMIN.name(),
                    tcAdminRequest.getCompany()
            );
            tcAdmin.setVerifyToken(generateEmailUUID());
            tcAdmin.setVerifyDate(String.valueOf(LocalDate.now()));
            tcAdmin.setVerEnabled(false);
            tcAdminRepository.save(tcAdmin);
            sendEmail.submitSignupEmailExecution(tcAdmin);
            return ResponseEntity.ok(new TestVerifyResponse(tcAdmin.getVerifyToken()));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email is already in use.");
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
