package edu.uwp.appfactory.tow.controllers.driver;

import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.DriverRequest;
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
public class DriverService {
    @Autowired
    public DriverRepository driverRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AsyncEmailService sendEmail;

    public ResponseEntity<TestVerifyResponse> register(DriverRequest driverRequest) {
        if(!AccountInformationValidator.validateEmail(driverRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Typo in email");
        }
        List<String> errorPassword = AccountInformationValidator.validatePassword(driverRequest.getPassword());
        if(!errorPassword.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorPassword.toString());
        }
        if (!driverRepository.existsByEmail(driverRequest.getEmail())){
            Drivers drivers = new Drivers(
                    driverRequest.getEmail(),
                    driverRequest.getEmail(),
                    encoder.encode(driverRequest.getPassword()),
                    driverRequest.getFirstname(),
                    driverRequest.getLastname(),
                    driverRequest.getPhone(),
                    ERole.ROLE_DRIVER.name()
            );
            drivers.setVerifyToken(generateEmailUUID());
            drivers.setVerifyDate(String.valueOf(LocalDate.now()));
            drivers.setVerEnabled(false);
            driverRepository.save(drivers);
            sendEmail.submitEmailExecution(drivers);
            TestVerifyResponse testVerifyResponse = new TestVerifyResponse(drivers.getVerifyToken());
            return ResponseEntity.ok(testVerifyResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Generates a random 6 character length UUID.
     * @return UUID converted to a String
     */
    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
