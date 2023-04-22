package edu.uwp.appfactory.tow.controllers.policedepartment;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.PDAdminRequest;
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
public class PDAdminService {

    private final PDAdminRepository pdAdminRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmailService sendEmail;
    private final PasswordEncoder encoder;

    public PDAdminService(PDAdminRepository pdAdminRepository, UsersRepository usersRepository, AsyncEmailService sendEmail, PasswordEncoder encoder) {
        this.pdAdminRepository = pdAdminRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
    }

    /**
     * Retrieves a PDAdmin given the users UUID.
     *
     * @return - PdAdmin if UUID exists in database, null otherwise
     */
    public PDAdmin get(UserDetailsImpl userDetails) {
        return pdAdminRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
    }

    /**
     * Registers a new PDAdmin with the given information.
     *
     * @param pdAdminRequest The PDAdminRequest containing the information for the new PDAdmin.
     * @return A ResponseEntity containing the TestVerifyResponse with the generated verification token.
     * @throws ResponseStatusException if the email is already in use, if there's a typo in the email or if the password is invalid.
     */
    public ResponseEntity<TestVerifyResponse> register(PDAdminRequest pdAdminRequest) {
        if(usersRepository.existsByEmail(pdAdminRequest.getEmail())){
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        if(!AccountInformationValidator.validateEmail(pdAdminRequest.getEmail())){
            throw new ResponseStatusException(BAD_REQUEST,"Typo in email");
        }
        List<String> passwordViolations = AccountInformationValidator.validatePassword(pdAdminRequest.getPassword());
        if(!passwordViolations.isEmpty()){
            throw new ResponseStatusException(BAD_REQUEST, passwordViolations.toString());
        }
        PDAdmin pdAdmin = new PDAdmin(
                pdAdminRequest.getEmail(),
                pdAdminRequest.getEmail(),
                encoder.encode(pdAdminRequest.getPassword()),
                pdAdminRequest.getFirstname(),
                pdAdminRequest.getLastname(),
                pdAdminRequest.getPhone(),
                ERole.ROLE_PDADMIN.name(),
                pdAdminRequest.getCity(),
                pdAdminRequest.getAddressNumber(),
                pdAdminRequest.getDepartment(),
                pdAdminRequest.getDepartmentShort()
        );
        pdAdmin.setVerifyToken(generateEmailUUID());
        pdAdmin.setVerifyDate(String.valueOf(LocalDate.now()));
        pdAdmin.setVerEnabled(false);
        pdAdminRepository.save(pdAdmin);
        sendEmail.submitSignupEmailExecution(pdAdmin);
        TestVerifyResponse test = new TestVerifyResponse(pdAdmin.getVerifyToken());
        return ResponseEntity.ok(test);
    }

    /**
     * Generates a random UUID without "-".
     * @return UUID converted to a String
     */
    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
