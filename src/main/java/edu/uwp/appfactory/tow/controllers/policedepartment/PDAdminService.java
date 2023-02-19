package edu.uwp.appfactory.tow.controllers.policedepartment;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.PDAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
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
     * @param userId - UUID of user to be retrieved.
     * @return - PdAdmin if UUID exists in database, null otherwise
     */
    public PDAdmin get(UUID userId) {
        Optional<PDAdmin> user = pdAdminRepository.findById(userId);
        return user.orElse(null);
    }

    /**
     * Registers a new PD Admin. Only allows one unique email address per PD admin.
     * @param pdAdminRequest - PD admin account information
     * @return verification token if successful, otherwise, error 400
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
        usersRepository.save(pdAdmin);
        sendEmail.submitEmailExecution(pdAdmin);
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

    //todo add Patch and Delete
}
