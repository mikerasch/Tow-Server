
package edu.uwp.appfactory.tow.controllers.auth;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UserRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
     * Refreshes the given JWT token.
     *
     * @param jwtToken The JWT token to be refreshed.
     * @return ResponseEntity with a new JWT token if the refresh was successful, or a bad request response otherwise.
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") final String jwtToken) {
        String token = authService.refreshToken(jwtToken);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param loginRequest The login request object containing the user's email and password.
     * @return A ResponseEntity with a JwtResponse object containing the JWT token and token type if the authentication was successful, or a bad request response otherwise.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    /**
     * Verifies the user account using the provided verification token.
     *
     * @param token The verification token.
     * @return A ResponseEntity with a status of NO_CONTENT if the verification was successful, or a ResponseEntity with an appropriate error response if the verification was unsuccessful.
     * @throws ResponseStatusException if there is an unexpected error or if the verification failed due to an expired link or already verified user.
     */
    @GetMapping("/verification")
    public ResponseEntity<HttpStatus> verification(@RequestParam("token") final String token) {
        HttpStatus status = authService.verification(token);
        return switch (status) {
            case OK -> ResponseEntity.status(NO_CONTENT).build();
            case FORBIDDEN -> throw new ResponseStatusException(FORBIDDEN, "Link expired, account deleted");
            case GONE -> throw new ResponseStatusException(GONE, "User already verified");
            default -> throw new ResponseStatusException(BAD_REQUEST, "Resource not found");
        };
    }

    /**
     * Retrieves the user information for the authenticated user.
     *
     * @param userDetails The authenticated user details.
     * @return A ResponseEntity with a UserRequest object containing the user's information if the request is successful.
     */
    @GetMapping("/get/user")
    @PreAuthorize("hasAnyRole('PDADMIN','PDUSER','TCADMIN','TCUSER','SPADMIN','DRIVER')")
    public ResponseEntity<UserRequest> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return authService.getUserInformation(userDetails);
    }
}




