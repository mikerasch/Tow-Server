
package edu.uwp.appfactory.tow.controllers;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.AdminRequest;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.services.roles.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

/**
 * This class contains the Routes responsible for authorization.
 * Included paths are:
 * post - /refresh
 * post - /login
 * post - /admin
 * get - /verification
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Parameterized constructor for creating a new AuthController.
     * @param authService - service to access user repository and pdAdmin repository
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Refreshes the JWT Token. Requires prior authentication on post request.
     * @param jwtToken - jwt Token to be refreshed.
     * @return ResponseEntity, 200 if success, 400 otherwise
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") final String jwtToken) {
        String token = authService.refreshToken(jwtToken);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Handles requesting authentication from user.
     * @param loginRequest Login request information of user
     * @return Response entity which can be a JWTResponse or an HttpStatus
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    /**
     * Handles registering an administrator.
     * @param adminRequest Register request information of administrator
     * @return Response entity http status. NO_CONTENT is success, BAD_REQUEST is failure
     */
    @PostMapping("/admin")
    public ResponseEntity<HttpStatus> registerAdmin(@RequestBody AdminRequest adminRequest) {
        return authService.registerAdmin(adminRequest)
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(BAD_REQUEST).build();
    }

    /**
     * Handles verification of a user given a regular token.
     * Verification is the route that is hooked to a button the user receives by email in order to activate their account the first time.
     * Verification can fail due to user already verified, link expiring, or token not found.
     * @param token - Token to verify
     * @return Response entity: 204 if success, String response otherwise
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




