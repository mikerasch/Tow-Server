package edu.uwp.appfactory.tow.controllers.auth;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.AdminRequest;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UserRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
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
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    public AuthService(AuthenticationManager authenticationManager, UsersRepository usersRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.encoder = encoder;
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
     * Used for authentication a user.
     * There are four possibilities: user does not exist in DB, user is not permitted, user not verified, or
     * successfully authenticated.
     *
     * @param loginRequest - login user information to authenticate.
     * @return Response Entity of user information if success, else BAD_REQUEST or UNAUTHORIZED
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
        Optional<Users> usersOptional = Optional.of(usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow());
        Users user = usersOptional.get();
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
    // todo make this not awful
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

    public ResponseEntity<UserRequest> getUserInformation(UserDetailsImpl userDetails) {
        Optional<Users> usersOptional = Optional.of(usersRepository.findById(userDetails.getId()).orElseThrow());
        Users users = usersOptional.get();
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