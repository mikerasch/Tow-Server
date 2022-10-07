
package edu.uwp.appfactory.tow.controllers;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.AdminRequest;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.services.roles.AuthService;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

/**
 * This class contains the Routes responsible for authorization. All are relatively self explanatory except verification.
 * verification is the route that is hooked to a button the user receives by email in order to activate their account the first time..
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") final String jwtToken) {
        String token = authService.refreshToken(jwtToken);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRequest adminRequest) {
        return authService.registerAdmin(adminRequest)
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(BAD_REQUEST).body("Error");
    }

    /**
     * uses the token sent to the users email in order to verify they have access to the desired email address.
     */
    @GetMapping("/verification")
    public ResponseEntity<?> verification(@RequestParam("token") final String token) {
        HttpStatus status = authService.verification(token);
        return switch (status) {
            case OK -> ResponseEntity.status(NO_CONTENT).build();
            case FORBIDDEN -> ResponseEntity.status(FORBIDDEN).body("Link expired, account deleted");
            case GONE -> ResponseEntity.status(GONE).body("User already verified");
            default -> ResponseEntity.status(NOT_FOUND).body("Resource not found");
        };
    }
}




