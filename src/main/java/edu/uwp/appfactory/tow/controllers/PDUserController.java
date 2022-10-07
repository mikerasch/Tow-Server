package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.services.roles.PDUserService;
import edu.uwp.appfactory.tow.services.roles.UserService;
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
 * this class is used by pd admins to register and maintain PD user accounts.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pdusers")
public class PDUserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PDUserService pdUserService;

    public PDUserController(UserService userService, JwtUtils jwtUtils, PDUserService pdUserService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.pdUserService = pdUserService;
    }

    /**
     * GET method that returns user based off of the UUID from the JWT token. Currently only
     *     accessible by tc users, for gets with all auth options look at userroutes.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('PDUSER')")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        PDUser data = pdUserService.get(UUID.fromString(userId));
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
    @PostMapping("")
    @PreAuthorize("hasRole('PDADMIN')")
    public ResponseEntity<?> register(@RequestHeader("Authorization") final String jwtToken,
                                      @RequestBody PDUserRequest pdUserRequest) {
        PDUAuthResponse data = pdUserService.register(pdUserRequest, jwtToken);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }


    /**
     * PATCH
     */


    /**
     * DELETE
     */
}
