package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.services.roles.PDUserService;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.PDUserRequest;
import edu.uwp.appfactory.tow.responseObjects.PDUAuthResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Used to create police department user accounts requiring a PD admin to register.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pdusers")
public class PDUserController {
    private final JwtUtils jwtUtils;
    private final PDUserService pdUserService;

    /**
     * Parameterized constructor for creating a new PdUserController.
     * @param jwtUtils - handling management of JWT tokens for security
     * @param pdUserService - service to access/provide useful police department USER logic
     */
    public PDUserController(JwtUtils jwtUtils, PDUserService pdUserService) {
        this.jwtUtils = jwtUtils;
        this.pdUserService = pdUserService;
    }

    /**
     * Returns user based off the UUID from the JWT token. Only usable currently by TC users.
     * For all other authentication options view the link below.
     * @param jwtToken - jwt token to be used for further authorization, needs to be valid see below to refresh
     * @return UUID information if successful, 400 otherwise
     * @see edu.uwp.appfactory.tow.controllers.UserController
     * @see edu.uwp.appfactory.tow.controllers.AuthController#refreshToken(String)
     */
    @GetMapping("")
    @PreAuthorize("hasRole('PDUSER')")
    public ResponseEntity<PDUser> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        PDUser data = pdUserService.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Registers a new PD user using the PD admins jwt for further authentication.
     * @param jwtToken - jwt token to be used for further authorization, needs to be valid see below to refresh
     * @param pdUserRequest - contains the information of a user
     * @return information relating to the newly created PD user, 400 otherwise
     * @see edu.uwp.appfactory.tow.controllers.AuthController#refreshToken(String)
     */
    @PostMapping("")
    @PreAuthorize("hasRole('PDADMIN')")
    public ResponseEntity<PDUAuthResponse> register(@RequestHeader("Authorization") final String jwtToken,
                                      @RequestBody PDUserRequest pdUserRequest) {
        return pdUserService.register(pdUserRequest, jwtToken);
    }
    //todo add Patch and Delete
}
