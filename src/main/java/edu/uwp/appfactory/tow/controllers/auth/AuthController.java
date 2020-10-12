package edu.uwp.appfactory.tow.controllers.auth;

import edu.uwp.appfactory.tow.entities.Dispatcher;
import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.Role;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.RoleRepository;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> refreshToken(String jwtToken) {
        return ResponseEntity.ok(jwtUtils.refreshJwtToken(jwtUtils.getUUIDFromJwtToken(jwtToken)));
    }

    public ResponseEntity<?> getUserByEmail(String email) {
        Users user = usersRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity
                    .status(500)
                    .body(new MessageResponse("Error: User does not exist or does not have role of user!"));
        }

        user.setPassword("");

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> deleteUserById(String email) {
        Users users = usersRepository.findByEmail(email);

        if (users == null) {
            return ResponseEntity
                    .status(500)
                    .body(new MessageResponse("Not successful!"));
        } else {
            usersRepository.delete(users);
            return ResponseEntity
                    .status(200)
                    .body(new MessageResponse("Successful!"));
        }
    }

    public ResponseEntity<?> authenticateUser(String email, String password) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUUID(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstname(),
                userDetails.getLastname(),
                userDetails.getRole()));
    }

    public ResponseEntity<?> registerDriver(String email, String password, String firstname, String lastname, String business, String cdlLicenceNumber) {
        try {
            if (usersRepository.existsByEmail(email)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            // Create new user's account
            Driver driver = new Driver(email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    business,
                    cdlLicenceNumber);

            Role role = roleRepository.findByName(ERole.ROLE_DRIVER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            driver.setRoles(role.getName().toString());
            usersRepository.save(driver);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(498).body("Invalid Entries: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(499).body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> registerAdmin(String email, String password, String firstname, String lastname) {

        if (usersRepository.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Users user = new Users(email,
                email,
                encoder.encode(password),
                firstname,
                lastname);

        Role role = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(role.getName().toString());
        usersRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    public ResponseEntity<?> registerDispatcher(String email, String password, String firstname, String lastname, String precinct) {
        try {
            if (usersRepository.existsByEmail(email)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            // Create new user's account
            Dispatcher dispatcher = new Dispatcher(
                    email,
                    email,
                    encoder.encode(password),
                    firstname,
                    lastname,
                    precinct);

            Role role = roleRepository.findByName(ERole.ROLE_DISPATCHER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            dispatcher.setRoles(role.getName().toString());
            usersRepository.save(dispatcher);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (TransactionSystemException | ConstraintViolationException e) {
            return ResponseEntity.status(499).body("Invalid Entries");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("System Error");
        }
    }
}