package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.mappers.PDMapper;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.PDAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class PDAdminService {

    private final PDAdminRepository pdAdminRepository;
    private final UsersRepository usersRepository;
    private final AsyncEmailService sendEmail;
    private final PasswordEncoder encoder;
    private final PDMapper pdMapper;


    @Autowired
    public PDAdminService(PDAdminRepository pdAdminRepository, UsersRepository usersRepository, AsyncEmailService sendEmail, PasswordEncoder encoder, PDMapper pdMapper) {
        this.pdAdminRepository = pdAdminRepository;
        this.usersRepository = usersRepository;
        this.sendEmail = sendEmail;
        this.encoder = encoder;
        this.pdMapper = pdMapper;
    }

    /**
     * GET
     */
    public PDAdmin get(UUID userId) {
        Optional<PDAdmin> user = pdAdminRepository.findById(userId);
        return user.orElse(null);
    }


    /**
     * POST
     */
    public ResponseEntity<?> register(PDAdminRequest pdAdminRequest) {
        if(usersRepository.existsByEmail(pdAdminRequest.getEmail())){
            return ResponseEntity.status(BAD_REQUEST).build();
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
        sendEmail.sendEmailAsync(pdAdmin);
        TestVerifyResponse test = new TestVerifyResponse(pdAdmin.getVerifyToken());
        return ResponseEntity.ok(test);
    }

    /**
     * PATCH
     */


    /**
     * DELETE
     */


    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
