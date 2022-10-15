package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.services.roles.TCUserService;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.TCUserRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Register and maintain tc user accounts. Only TC Admins can access these routes.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcusers")
public class TCUserController {
    private final JwtUtils jwtUtils;
    private final TCUserService tcUserService;

    /**
     * Parameterized constructor for creating a new TCUserController.
     * @param jwtUtils - handling management of JWT tokens for security
     * @param tcUserService - service to handle registration and maintenance of tow company users.
     */
    public TCUserController(JwtUtils jwtUtils, TCUserService tcUserService) {
        this.jwtUtils = jwtUtils;
        this.tcUserService = tcUserService;
    }

    /**
     * Retrieves user based off the UUID from the JWT token.
     * Only usable by tc users, for other auth options, see below.
     * @param jwtToken jwt token of tc user
     * @return UUID information of TC user, else 400
     * @see edu.uwp.appfactory.tow.controllers.UserController
     */
    @GetMapping("")
    @PreAuthorize("hasRole('TCUSER')")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        TCUser data = tcUserService.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves all user based off the UUID from the JWT token.
     * @param jwtToken Only usable by TC admins, for other auth options, see below
     * @return list of all tow company users, else 400
     * @see edu.uwp.appfactory.tow.controllers.UserController
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('TCADMIN')")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") final String jwtToken) {
        UUID adminUUID = UUID.fromString(jwtUtils.getUUIDFromJwtToken(jwtToken));
        List<TCUser> data = tcUserService.getAll(adminUUID);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }


    /**
     * POST method that uses the users jwt for the admin preauth and the pdUserRequest object that contains
     * the information of a user.
     */
    /**
     * Registers a new tow company user.
     * @param jwtToken - jwt token of tc administrator
     * @param tcUserRequest contains information of new tow company user to be added
     * @return new tow company users token, else 400
     */
    @PreAuthorize("hasRole('TCADMIN')")
    @PostMapping("")
    public ResponseEntity<?> register(@RequestHeader("Authorization") final String jwtToken,
                                      @RequestBody TCUserRequest tcUserRequest) {
        UUID adminUUID = UUID.fromString(jwtUtils.getUUIDFromJwtToken(jwtToken));
        return tcUserService.register(tcUserRequest, adminUUID);
    }
    //todo add Patch and Delete
}
