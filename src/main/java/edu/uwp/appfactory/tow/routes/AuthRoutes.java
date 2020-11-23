package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import edu.uwp.appfactory.tow.controllers.auth.AuthController;

/**
 * @author: Gianluca Eickenberg, Colin Hoffman
 * @modifiedBy: Quincy Kayle, Matthew Rank
 * * this class contains all of the routes that the drivers, dispatchers,
 * * and admins may hit in order to create their account.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class AuthRoutes {

    private final AuthController authController;

    public AuthRoutes(AuthController authController) {
        this.authController = authController;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('DRIVER') or hasRole('DISPATCHER')")
    public ResponseEntity<?> getUserByEmail(@RequestHeader("email") final String email) {
        return authController.getUserByEmail(email);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@RequestHeader("email") final String email) {
        return authController.deleteUserById(email);
    }

    @PatchMapping(value = "/")
    public ResponseEntity<?> update(@RequestHeader("Authorization") final String jwtToken,
                                    @RequestBody Users user) {
        return authController.update(jwtToken, user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestHeader("email") final String email,
                                              @RequestHeader("password") final String password) {
        return authController.authenticateUser(email, password);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") final String jwtToken) {
        return authController.refreshToken(jwtToken);
    }

    @PostMapping("/registerdriver")
    public ResponseEntity<?> registerDriver(@RequestHeader("email") final String email,
                                            @RequestHeader("password") final String password,
                                            @RequestHeader("firstname") final String firstname,
                                            @RequestHeader("lastname") final String lastname,
                                            @RequestHeader("phone") final String phone) {
        return authController.registerDriver(email, password, firstname, lastname, phone);
    }

    @PostMapping("/registerdispatcher")
    public ResponseEntity<?> registerDispatcher(@RequestHeader("email") final String email,
                                                @RequestHeader("password") final String password,
                                                @RequestHeader("firstname") final String firstname,
                                                @RequestHeader("lastname") final String lastname,
                                                @RequestHeader("precinct") final String precinct,
                                                @RequestHeader("phone") final String phone) {
        return authController.registerDispatcher(email, password, firstname, lastname, phone, precinct);
    }

    @PostMapping("/registeradmin")
    public ResponseEntity<?> registerAdmin(@RequestHeader("email") final String email,
                                           @RequestHeader("password") final String password,
                                           @RequestHeader("firstname") final String firstname,
                                           @RequestHeader("lastname") final String lastname,
                                           @RequestHeader("phone") final String phone) {
        return authController.registerAdmin(email, password, firstname, lastname, phone);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verification(@RequestParam("token") final String token) {
        return authController.verification(token);
    }
}