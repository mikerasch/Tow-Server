package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.PDUserController;
import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.requestObjects.PDUserRequest;
import edu.uwp.appfactory.tow.responseObjects.PDUAuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pdusers")
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
    @GetMapping("")
    @PreAuthorize("hasRole('PDUSER')")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        PDUser data = pdUserController.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }


    /**
     * POST
     */
    @PostMapping("")
    @PreAuthorize("hasRole('PDADMIN')")
    public ResponseEntity<?> register(@RequestHeader("Authorization") final String jwtToken,
                                      @RequestBody PDUserRequest pdUserRequest) {
        PDUAuthResponse data = pdUserController.register(pdUserRequest, jwtToken);
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
