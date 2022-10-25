package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.SuperAdmin;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.AdminRequest;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    private final SuperAdminRepository superAdminRepository;
    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UsersRepository usersRepository, PasswordEncoder encoder, JwtUtils jwtUtils, SuperAdminRepository superAdminRepository) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.superAdminRepository = superAdminRepository;
    }

    /**
     * Used to refresh a jwtToken.
     *
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
     *
     * @param loginRequest - login user information to authenticate.
     * @return Response Entity of user information if success, else BAD_REQUEST or UNAUTHORIZED
     */
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationRequest(loginRequest);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Users> usersOptional = usersRepository.findByEmail(loginRequest.getEmail());
        if (usersOptional.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "User does not exist");
        }
        //todo: when not testing, uncomment code
        if (userDetails.getRole().equals(loginRequest.getPlatform())) { // this line checks that the user attempting to log in is on the correct client app
            Users user = usersOptional.get();
            boolean verEnabled = user.getVerEnabled();
            if (verEnabled) {
                return ResponseEntity.ok(new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getFirstname(),
                        userDetails.getLastname(),
                        userDetails.getRole(),
                        userDetails.getPhone())
                );
            } else {
                throw new ResponseStatusException(BAD_REQUEST, "User not verified");
            }
        } else {
            throw new ResponseStatusException(UNAUTHORIZED, "User is not permitted to use this dashboard");
        }
    }

    public Authentication authenticationRequest(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    //todo add verification from email. Needs to be custom
    public ResponseEntity<JwtResponse> authenticateSuperAdmin(LoginRequest loginRequest) {
        Authentication authentication = authenticationRequest(loginRequest);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<SuperAdmin> superAdminQuery = superAdminRepository.findByEmail(loginRequest.getEmail());
        if(superAdminQuery.isEmpty()){
            throw new ResponseStatusException(BAD_REQUEST,"User does not exist");
        }
        if (userDetails.getRole().equals(loginRequest.getPlatform())) { // this line checks that the user attempting to log in is on the correct client app
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getFirstname(),
                    userDetails.getLastname(),
                    userDetails.getRole(),
                    userDetails.getPhone()
            ));
        } else {
            throw new ResponseStatusException(UNAUTHORIZED, "User is not permitted to use this dashboard");
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
     *
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