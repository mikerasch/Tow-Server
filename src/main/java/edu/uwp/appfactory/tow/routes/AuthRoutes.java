package edu.uwp.appfactory.tow.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import edu.uwp.appfactory.tow.controllers.auth.AuthController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class AuthRoutes {

    private final AuthController authController;

    public AuthRoutes(AuthController authController) {
        this.authController = authController;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserByEmail(@RequestHeader("email") final String email) {
        return authController.getUserByEmail(email);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@RequestHeader("email") final String email) {
        return authController.deleteUserById(email);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestHeader("email") final String email, @RequestHeader("password") final String password) {
        return authController.authenticateUser(email, password);
    }

    @PostMapping("/registeruser")
    public ResponseEntity<?> registerUser(@RequestHeader("email") final String email,
                                          @RequestHeader("password") final String password,
                                          @RequestHeader("firstname") final String firstname,
                                          @RequestHeader("lastname") final String lastname) {
        return authController.registerUser(email, password, firstname, lastname);
    }

    @PostMapping("/registertest")
    public ResponseEntity<?> registerTest(@RequestHeader("email") final String email,
                                          @RequestHeader("password") final String password,
                                          @RequestHeader("firstname") final String firstname,
                                          @RequestHeader("lastname") final String lastname,
                                          @RequestHeader("precinct") final String precinct) {
        return authController.registerTest(email, password, firstname, lastname, precinct);
    }

    @PostMapping("/registeradmin")
    public ResponseEntity<?> registerAdmin(@RequestHeader("email") final String email,
                                           @RequestHeader("password") final String password,
                                           @RequestHeader("firstname") final String firstname,
                                           @RequestHeader("lastname") final String lastname) {
        return authController.registerAdmin(email, password, firstname, lastname);
    }
}