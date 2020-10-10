package edu.uwp.appfactory.tow.controllers.auth;

import edu.uwp.appfactory.tow.testingEntities.DispatcherUsers;
import edu.uwp.appfactory.tow.testingEntities.Users;
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
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstname(),
                userDetails.getLastname(),
                userDetails.getRole()));
    }

    public ResponseEntity<?> registerUser(String email, String password, String firstname, String lastname) {

        if (usersRepository.existsByUsername(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

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

        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(role.getName().toString());
        usersRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> registerAdmin(String email, String password, String firstname, String lastname) {

        if (usersRepository.existsByUsername(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

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

    public ResponseEntity<?> registerTest(String email, String password, String firstname, String lastname, String precinct) {

        if (usersRepository.existsByUsername(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (usersRepository.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        DispatcherUsers user = new DispatcherUsers(
                email,
                email,
                encoder.encode(password),
                firstname,
                lastname,
                precinct);

        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(role.getName().toString());
        usersRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestHeader("email") final String email,
//                                          @RequestHeader("password") final String password,
//                                          @RequestHeader("firstname") final String firstname,
//                                          @RequestHeader("lastname") final String lastname,
//                                          @RequestHeader("role") final String reqRoles) {
//
//        if (usersRepository.existsByUsername(email)) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (usersRepository.existsByEmail(email)) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        // Create new user's account
//        Users user = new Users(email,
//                email,
//                encoder.encode(password),
//                firstname,
//                lastname);
//
//        Role role;
//
//		if (reqRoles.equals("admin")) {
//			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//					.orElseThrow(() -> new RuntimeException("Error: Role is not found. Admin"));
//			role = adminRole;
//		} else if (reqRoles.equals("dispatcher")) {
//			Role dispatcherRole = roleRepository.findByName(ERole.ROLE_DISPATCHER)
//					.orElseThrow(() -> new RuntimeException("Error: Role is not found. Dispatcher"));
//			role = dispatcherRole;
//		} else {
//			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//					.orElseThrow(() -> new RuntimeException("Error: Role is not found. User"));
//			role = userRole ;
//		}
//
//
//        user.setRoles(role.getName().toString());
//        usersRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }