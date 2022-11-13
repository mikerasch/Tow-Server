package edu.uwp.appfactory.tow.controllers.superAdmin;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SuperAdminService {
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AsyncEmailService sendEmail;
    @Autowired
    public SuperAdminService(SuperAdminRepository superAdminRepository, PasswordEncoder passwordEncoder,AsyncEmailService sendEmail){
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendEmail = sendEmail;
    }

    public ResponseEntity<TestVerifyResponse> register(SuperAdminRequest spAdminRequest) {
        if(superAdminRepository.existsByEmail(spAdminRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already exists!");
        }
        List<String> passwordViolations = AccountInformationValidator.validatePassword(spAdminRequest.getPassword());
        if(!passwordViolations.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,passwordViolations.toString());
        }
        SPAdmin superAdmin = new SPAdmin(
                spAdminRequest.getFirstname(),
                spAdminRequest.getLastname(),
                spAdminRequest.getEmail(),
                passwordEncoder.encode(spAdminRequest.getPassword()),
                spAdminRequest.getPhone(),
                ERole.ROLE_SPADMIN.name(),
                spAdminRequest.getUsername()
        );
        superAdmin.setVerifyToken(generateEmailUUID());
        superAdmin.setVerifyDate(String.valueOf(LocalDate.now()));
        superAdmin.setVerEnabled(false);
        superAdminRepository.save(superAdmin);
        sendEmail.submitEmailExecution(superAdmin);
        TestVerifyResponse test = new TestVerifyResponse(superAdmin.getVerifyToken());
        return ResponseEntity.ok(test);
    }

    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
