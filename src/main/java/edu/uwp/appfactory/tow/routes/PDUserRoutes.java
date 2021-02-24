package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.PDUserController;
import edu.uwp.appfactory.tow.controllers.TCUserController;
import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.requestObjects.PDUAuthRequest;
import edu.uwp.appfactory.tow.requestObjects.PDUserRequest;
import edu.uwp.appfactory.tow.requestObjects.TCUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcuser")
public class PDUserRoutes {

    private final UserController userController;
    private final JwtUtils jwtUtils;
    private final PDUserController pdUserController;

    public PDUserRoutes(UserController userController, JwtUtils jwtUtils, PDUserController pdUserController) {
        this.userController = userController;
        this.jwtUtils = jwtUtils;
        this.pdUserController = pdUserController;
    }

    /**
     * GET
     */


    /**
     * POST
     */
    @PreAuthorize("hasRole('PDADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> register(@RequestHeader("Authorization") final String jwtToken,
                                            @RequestBody PDUserRequest pdUserRequest) {
        UUID adminUUID = UUID.fromString(jwtUtils.getUUIDFromJwtToken(jwtToken));
        PDUAuthRequest data = pdUserController.register(pdUserRequest, adminUUID);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }


    /**
     * PATCH
     */


    /**
     * DELETE
     */
}
