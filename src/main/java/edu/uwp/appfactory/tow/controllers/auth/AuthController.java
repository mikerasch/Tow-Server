package edu.uwp.appfactory.tow.controllers.auth;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.services.UserDetailsImpl;
import edu.uwp.appfactory.tow.entities.*;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.*;
import edu.uwp.appfactory.tow.services.AsyncEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    //access tokens expire quickly
    //clients ask for new one based on their refresh token
    //if refresh token is expired, re-auth
    //logout route
    //JSON WEB TOKEN NPM

    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final PDAdminRepository pdAdminRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final AsyncEmail sendEmail;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UsersRepository usersRepository, PDAdminRepository pdAdminRepository, PasswordEncoder encoder, JwtUtils jwtUtils, AsyncEmail sendEmail) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.pdAdminRepository = pdAdminRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.sendEmail = sendEmail;
    }

    public String refreshToken(String jwtToken) {
        return jwtUtils.refreshJwtToken(jwtUtils.getUUIDFromJwtToken(jwtToken));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Users> usersOptional = usersRepository.findByUsername(loginRequest.getEmail());

        //todo: when not testing, uncomment code
        if (userDetails.getRole().equals(loginRequest.getPlatform())) {
            if (usersOptional.isPresent()) {
                Users user = usersOptional.get();
                return user.getVerEnabled() ?
                        ResponseEntity.ok(new JwtResponse(
                                jwt,
                                userDetails.getId(),
                                userDetails.getUsername(),
                                userDetails.getEmail(),
                                userDetails.getFirstname(),
                                userDetails.getLastname(),
                                userDetails.getRole())
                        ) :
                        ResponseEntity.badRequest().body(new MessageResponse("User not verified"));
            } else {
                return ResponseEntity
                        .status(494)
                        .body(new MessageResponse("User does not exist"));
            }
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("User is not permitted to use this dashboard"));
        }
    }

    public boolean registerAdmin(AdminRequest adminRequest) {
        if (!usersRepository.existsByEmail(adminRequest.getEmail())) {
            Users user = new Users(adminRequest.getEmail(),
                    adminRequest.getEmail(),
                    encoder.encode(adminRequest.getPassword()),
                    adminRequest.getFirstname(),
                    adminRequest.getLastname(),
                    adminRequest.getPhone(),
                    ERole.ROLE_ADMIN.name());

            usersRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * PD
     */

    public ResponseEntity<?> registerPDAdmin(PDAdminRequest pdAdminRequest) {
        if (!usersRepository.existsByEmail(pdAdminRequest.getEmail())) {
            PDAdmin pdAdmin = new PDAdmin(pdAdminRequest.getEmail(),
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
            return ResponseEntity.ok(pdAdmin.getVerifyToken());
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }

    public boolean registerPDUser(PDUserRequest pdUserRequest, UUID adminUUID) {
        if (!usersRepository.existsByEmail(pdUserRequest.getEmail())) {

            String frontID = "";

            Optional<PDAdmin> adminsOptional = pdAdminRepository.findById(adminUUID);
            if (adminsOptional.isPresent()) {
                PDAdmin admin = adminsOptional.get();
                frontID = admin.getDepartmentShort() + "-" + generatePDUserUUID();
            }

            PDUser pdUser = new PDUser(pdUserRequest.getEmail(),
                    pdUserRequest.getEmail(),
                    generatePDUserUUID(),
                    pdUserRequest.getFirstname(),
                    pdUserRequest.getLastname(),
                    "",
                    ERole.ROLE_PDUSER.name(),
                    frontID,
                    adminUUID);

            pdUser.setVerifyToken("");
            pdUser.setVerifyDate(String.valueOf(LocalDate.now()));
            pdUser.setVerEnabled(true);
            usersRepository.save(pdUser);
            return true;
        } else {
            return false;
        }
    }

    /**
     * TC
     */

    public ResponseEntity<?> registerTCAdmin(TCAdminRequest tcAdminRequest) {
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
            return ResponseEntity.ok(tcAdmin.getVerifyToken());
        } else {
            return ResponseEntity.status(400).build(); //TODO:Not sure if .build is correct
        }
    }

    public ResponseEntity<?> registerTCUser(TCUserRequest tcUserRequest, UUID adminUUID) {
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
            sendEmail.sendEmailAsync(tcuser);
            return ResponseEntity.ok(tcuser.getVerifyToken());
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }

    public int verification(String token) {
        Optional<Users> usersOptional = usersRepository.findByVerifyToken(token);
        if (usersOptional.isPresent()) {

            Users user = usersOptional.get();
            LocalDate userVerifyDate = LocalDate.parse(user.getVerifyDate());
            Period periodBetween = Period.between(userVerifyDate, LocalDate.now());

            if (periodBetween.getDays() < 8) {
                if (user.getVerifyToken().equals(token) && !user.getVerEnabled()) {
                    usersRepository.updateUserEmailVerifiedByUUID(user.getId(), true);
                    return 200; // success
                } else {
                    return 410; // user already verified
                }
            } else {
                usersRepository.deleteByEmail(user.getEmail());
                return 403; // expired, account deleted
            }
        } else {
            return 404; // token doesnt exist
        }
    }

    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String generatePDUserUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}