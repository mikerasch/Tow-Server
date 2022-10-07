package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.services.roles.TCAdminService;
import edu.uwp.appfactory.tow.services.roles.UserService;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.TCAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TCAdminResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
/**
 * this class is responsible for registering and retrieving PD admin information.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcadmins")
public class TCAdminController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final TCAdminService tcAdminService;

    public TCAdminController(UserService userService, JwtUtils jwtUtils, TCAdminService tcAdminService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.tcAdminService = tcAdminService;
    }

    /**
     * This route retrieves information about a requested tow truck admin
     * @param jwtToken the PDAdmins token so we can locate using the UUID
     * @return returns an object that contains only the necessary information about the admin.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('TCADMIN')")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        TCAdminResponse data = tcAdminService.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Registers a new tc admin in the system. Will need to be safeguarded in the future somehow. perhaps a godfather admin for
     * both other types of admins.
     */
    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody TCAdminRequest tcAdminRequest) {
        return tcAdminService.register(tcAdminRequest);

    }


    /**
     * PATCH
     */


    /**
     * DELETE
     */
}
