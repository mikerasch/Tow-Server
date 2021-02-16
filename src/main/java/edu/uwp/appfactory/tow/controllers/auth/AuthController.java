package edu.uwp.appfactory.tow.controllers.auth;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.services.UserDetailsImpl;
import edu.uwp.appfactory.tow.entities.*;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.PDAdminRequest;
import edu.uwp.appfactory.tow.requestObjects.PDUserRequest;
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

    public ResponseEntity<?> authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Users> usersOptional = usersRepository.findByUsername(email);

        //todo: when not testing, uncomment code
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
    }

    public boolean registerAdmin(String email, String password, String firstname, String lastname, String phone) {
        if (!usersRepository.existsByEmail(email)) {
            Users user = new Users(email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    phone,
                    ERole.ROLE_ADMIN.name());

            usersRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerDriver(String email, String password, String firstname, String lastname, String phone) {
        if (!usersRepository.existsByEmail(email)) {
            Driver driver = new Driver(email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    phone,
                    ERole.ROLE_DRIVER.name(),
                    0,
                    0,
                    false);

            driver.setVerifyToken(generateEmailUUID());
            driver.setVerifyDate(String.valueOf(LocalDate.now()));
            driver.setVerEnabled(false);
            usersRepository.save(driver);
            sendEmail.sendEmailAsync(driver);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerPDAdmin(PDAdminRequest pdAdminRequest) {
        if (!usersRepository.existsByEmail(pdAdminRequest.getEmail())) {
            PDAdmin pdadmin = new PDAdmin(pdAdminRequest.getEmail(),
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

            pdadmin.setVerifyToken(generateEmailUUID());
            pdadmin.setVerifyDate(String.valueOf(LocalDate.now()));
            pdadmin.setVerEnabled(false);
            usersRepository.save(pdadmin);
            sendEmail.sendEmailAsync(pdadmin);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerPDUser(PDUserRequest pdUserRequest, String adminUUID) {
        if (!usersRepository.existsByEmail(pdUserRequest.getEmail())) {

            String frontID;

            Optional<PDAdmin> usersOptional = pdAdminRepository.findById(UUID.fromString(adminUUID));
                    if (usersOptional.isPresent()) {
                        Users user = usersOptional.get();
                        frontID = user.get
                    }

                    PDUser pduser = new PDUser(pdUserRequest.getEmail(),
                    pdUserRequest.getEmail(),
                    generatePDUserUUID(),
                    pdUserRequest.getFirstname(),
                    pdUserRequest.getLastname(),
                    pdUserRequest.getPhone(),
                    ERole.ROLE_PDUSER.name(),
                    frontID,
                    adminUUID);

            pduser.setVerifyToken("");
            pduser.setVerifyDate("");
            pduser.setVerEnabled(true);
            usersRepository.save(pduser);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerTCAdmin(String email, String password, String firstname, String lastname, String phone) {
        if (!usersRepository.existsByEmail(email)) {
            TCAdmin tcadmin = new TCAdmin(email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    phone,
                    ERole.ROLE_PDADMIN.name(),
                    "cool tow company");

            tcadmin.setVerifyToken(generateEmailUUID());
            tcadmin.setVerifyDate(String.valueOf(LocalDate.now()));
            tcadmin.setVerEnabled(false);
            usersRepository.save(tcadmin);
            sendEmail.sendEmailAsync(tcadmin);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerTCUser(String email, String password, String firstname, String lastname, String phone) {
        if (!usersRepository.existsByEmail(email)) {
            TCAdmin tcadmin = new TCAdmin(email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    phone,
                    ERole.ROLE_PDADMIN.name(),
                    "cool tow company");

            tcadmin.setVerifyToken(generateEmailUUID());
            tcadmin.setVerifyDate(String.valueOf(LocalDate.now()));
            tcadmin.setVerEnabled(false);
            usersRepository.save(tcadmin);
            sendEmail.sendEmailAsync(tcadmin);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerDispatcher(String email, String password, String firstname, String lastname, String phone) {
        if (!usersRepository.existsByEmail(email)) {
            Dispatcher dispatcher = new Dispatcher(
                    email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    phone,
                    ERole.ROLE_DISPATCHER.name(),
                    "");

            dispatcher.setVerifyToken(generateEmailUUID());
            dispatcher.setVerifyDate(String.valueOf(LocalDate.now()));
            dispatcher.setVerEnabled(false);
            usersRepository.save(dispatcher);
            sendEmail.sendEmailAsync(dispatcher);
            return true;
        } else {
            return false;
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
        return UUID.randomUUID().toString().replace("-", "").substring(0,6);
    }
}