package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.mappers.TCMapper;
import edu.uwp.appfactory.tow.repositories.TCAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.TCAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TCAdminResponse;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class TCAdminService {

    private final TCAdminRepository tcAdminRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmailService sendEmail;
    private final PasswordEncoder encoder;
    private final TCMapper tcMapper;


    @Autowired
    public TCAdminService(TCAdminRepository tcAdminRepository, UsersRepository usersRepository, AsyncEmailService sendEmail, PasswordEncoder encoder, TCMapper tcMapper) {
        this.tcAdminRepository = tcAdminRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
        this.tcMapper = tcMapper;
    }

    /**
     * Retrieves the TCAdmin using a UUID.
     * @param userId - UUID of TCAdmin to retrieve
     * @return user if UUID is present in database, otherwise null
     */
    public TCAdminResponse get(UUID userId) {
        Optional<TCAdmin> user = tcAdminRepository.findById(userId);
        return user.map(tcMapper::map).orElse(null);
    }

    /**
     * Registers a new TCAdmin.
     * @param tcAdminRequest - TCAdmin information to create a new account
     * @return token response of newly created account, otherwise 400 error
     */
    public ResponseEntity<?> register(TCAdminRequest tcAdminRequest) {
        if (!usersRepository.existsByEmail(tcAdminRequest.getEmail())) {
            TCAdmin tcAdmin = new TCAdmin(tcAdminRequest.getEmail(),
                    tcAdminRequest.getEmail(),
                    encoder.encode(tcAdminRequest.getPassword()),
                    tcAdminRequest.getFirstname(),
                    tcAdminRequest.getLastname(),
                    tcAdminRequest.getPhone(),
                    ERole.ROLE_TCADMIN.name(),
                    tcAdminRequest.getCompany());

            tcAdmin.setVerifyToken(generateEmailUUID());
            tcAdmin.setVerifyDate(String.valueOf(LocalDate.now()));
            tcAdmin.setVerEnabled(false);
            usersRepository.save(tcAdmin);
            sendEmail.sendEmailAsync(tcAdmin);
            TestVerifyResponse x = new TestVerifyResponse(tcAdmin.getVerifyToken());
            return ResponseEntity.ok(x);
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

    //todo add Patch and Delete
}
