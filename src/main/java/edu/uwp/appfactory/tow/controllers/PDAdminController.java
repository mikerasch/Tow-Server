package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.services.roles.PDAdminService;
import edu.uwp.appfactory.tow.services.roles.UserService;
import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.PDAdminRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Responsible for registering and retrieving PD admin information.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pdadmins")
public class PDAdminController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PDAdminService pdAdminService;

    /**
     * Parameterized constructor for creating a new PDAdminController instance.
     * @param userService - service to access/provide useful user logic
     * @param jwtUtils - handling management of JWT tokens for security
     * @param pdAdminService - service to access/provide useful police department logic
     */
    public PDAdminController(UserService userService, JwtUtils jwtUtils, PDAdminService pdAdminService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.pdAdminService = pdAdminService;
    }

    /**
     * This route retrieves information about a requested police department admin
     * @param jwtToken the PDAdmins token so we can locate using the UUID
     * @return returns an object that contains only the necessary information about the admin.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('PDADMIN')")
    public ResponseEntity<PDAdmin> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        PDAdmin data = pdAdminService.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    //todo Safeguard in the future.
    /**
     * Registers a new admin in the system.
     * @param pdAdminRequest - police department admin account information
     * @return verification token if successful, else 400
     */
    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody PDAdminRequest pdAdminRequest) {
        return pdAdminService.register(pdAdminRequest);
    }


}
