package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.TCAdminController;
import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.requestObjects.TCAdminRequest;
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
public class TCAdminRoutes {

    private final UserController userController;
    private final JwtUtils jwtUtils;
    private final TCAdminController tcAdminController;

    public TCAdminRoutes(UserController userController, JwtUtils jwtUtils, TCAdminController tcAdminController) {
        this.userController = userController;
        this.jwtUtils = jwtUtils;
        this.tcAdminController = tcAdminController;
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
        TCAdminResponse data = tcAdminController.get(UUID.fromString(userId));
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
        return tcAdminController.register(tcAdminRequest);

    }


    /**
     * PATCH
     */


    /**
     * DELETE
     */
}
