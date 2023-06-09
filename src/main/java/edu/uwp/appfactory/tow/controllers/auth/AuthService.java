package edu.uwp.appfactory.tow.controllers.auth;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UserRequest;
import edu.uwp.appfactory.tow.securityconfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.securityconfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.securityconfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
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

import static org.springframework.http.HttpStatus.*;

@Service
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final JwtUtils jwtUtils;
    public AuthService(AuthenticationManager authenticationManager, UsersRepository usersRepository, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.jwtUtils = jwtUtils;
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
     * Authenticates the user using the provided login request and generates a JWT token for the user.
     *
     * @param loginRequest The login request containing the user's email and password.
     * @return A ResponseEntity with a JwtResponse object containing the JWT token and user information if the authentication was successful.
     * @throws ResponseStatusException if there is an error during authentication or if the user is not verified.
     */
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationRequest(loginRequest);
        String jwt;
        try{
            jwt = jwtUtils.generateJwtToken(authentication);
        } catch(Exception e){
            log.warn("User {} tried authenticating, but failed to be verified", loginRequest.getEmail());
            throw new ResponseStatusException(BAD_REQUEST,"Could not authenticate");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Users user = usersRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Could not find user!"));
        boolean verEnabled = user.getVerEnabled();
        if(verEnabled){
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
        }
        log.debug("User {} could not be verified, this could be due to user not verifying through email upon registration", user.getEmail());
        throw new ResponseStatusException(BAD_REQUEST,"User not verified");
    }

    public Authentication authenticationRequest(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * Used by the verification route in the initial sign up email
     *
     * @param token the email users token
     * @return returns a status code that will indicates success or failure
     */
    public HttpStatus verification(String token) {
        Users user = usersRepository.findByVerifyToken(token)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Could not finder user!"));
        LocalDate userVerifyDate = LocalDate.parse(user.getVerifyDate());
        Period periodBetween = Period.between(userVerifyDate, LocalDate.now());

        if (periodBetween.getDays() > 8) {
            usersRepository.deleteByEmail(user.getEmail());
            return FORBIDDEN; // expired, account deleted
        }

        if (user.getVerifyToken().equals(token) && !user.getVerEnabled()) {
            usersRepository.updateUserEmailVerifiedByUUID(user.getId(), true);
            return OK; // success
        }

        return GONE; // user already verified
    }

    /**
     * Retrieves the user information for the authenticated user.
     *
     * @param userDetails The authenticated user details.
     * @return A ResponseEntity with a UserRequest object containing the user's information if the request is successful.
     */
    public ResponseEntity<UserRequest> getUserInformation(UserDetailsImpl userDetails) {
        Users users = usersRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Could not find user!"));
        UserRequest userRequest = new UserRequest(
                users.getEmail(),
                users.getPhone(),
                users.getFirstname(),
                users.getLastname(),
                users.getFbToken()
        );
        return ResponseEntity.ok(userRequest);
    }
}