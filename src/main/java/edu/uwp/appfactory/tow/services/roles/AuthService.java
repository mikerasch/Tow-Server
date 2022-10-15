package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.AdminRequest;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class AuthService {

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
    private final AsyncEmailService sendEmail;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UsersRepository usersRepository, PDAdminRepository pdAdminRepository, PasswordEncoder encoder, JwtUtils jwtUtils, AsyncEmailService sendEmail) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.pdAdminRepository = pdAdminRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.sendEmail = sendEmail;
    }

    /**
     * Used to refresh a jwtToken.
     * @param jwtToken - jwtToken to be refreshed.
     * @return - new jwt Token
     */
    public String refreshToken(String jwtToken) {
        return jwtUtils.refreshJwtToken(jwtUtils.getUUIDFromJwtToken(jwtToken));
    }

    /**
     * Used for authentication a user.
     * There are four possibilities: user does not exist in DB, user is not permitted, user not verified, or
     * successfully authenticated.
     * @param loginRequest - login user information to authenticate.
     * @return Response Entity of user information if success, else BAD_REQUEST or UNAUTHORIZED
     */
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<Users> usersOptional = usersRepository.findByEmail(loginRequest.getEmail());

        //todo: when not testing, uncomment code
        if (userDetails.getRole().equals(loginRequest.getPlatform())) { // this line checks that the user attempting to log in is on the correct client app
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
                                userDetails.getRole(),
                                userDetails.getPhone())
                        ) :
                        ResponseEntity.badRequest().body(new MessageResponse("User not verified"));
            } else {
                return ResponseEntity
                        .status(BAD_REQUEST)
                        .body(new MessageResponse("User does not exist"));
            }
        } else {
            return ResponseEntity
                    .status(UNAUTHORIZED)
                    .body(new MessageResponse("User is not permitted to use this dashboard"));
        }
    }

    /**
     * Registration of a new Admin.
     * @param adminRequest - admin information to begin authorization process
     * @return - true if successfully registered, false otherwise
     */
    public boolean registerAdmin(AdminRequest adminRequest) {
        if (usersRepository.existsByEmail(adminRequest.getEmail())) {
            Users user = new Users(adminRequest.getEmail(),
                    adminRequest.getEmail(),
                    encoder.encode(adminRequest.getPassword()),
                    adminRequest.getFirstname(),
                    adminRequest.getLastname(),
                    adminRequest.getPhone(),
                    ERole.ROLE_ADMIN.name());
            user.setVerEnabled(true);

            usersRepository.save(user);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Used by the verification route in the initial sign up email
     * @param token the email users token
     * @return returns a status code that will indicates success or failure
     */
    public HttpStatus verification(String token) {
        Optional<Users> usersOptional = usersRepository.findByVerifyToken(token);
        if (usersOptional.isPresent()) {

            Users user = usersOptional.get();
            LocalDate userVerifyDate = LocalDate.parse(user.getVerifyDate());
            Period periodBetween = Period.between(userVerifyDate, LocalDate.now());

            if (periodBetween.getDays() < 8) {
                if (user.getVerifyToken().equals(token) && !user.getVerEnabled()) {
                    usersRepository.updateUserEmailVerifiedByUUID(user.getId(), true);
                    return OK; // success
                } else {
                    return GONE; // user already verified
                }
            } else {
                usersRepository.deleteByEmail(user.getEmail());
                return FORBIDDEN; // expired, account deleted
            }
        } else {
            return NOT_FOUND; // token doesnt exist
        }
    }
}