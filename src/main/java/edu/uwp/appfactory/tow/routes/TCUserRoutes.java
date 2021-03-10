package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.TCUserController;
import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.requestObjects.TCUserRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcusers")
public class TCUserRoutes {

    private final UserController userController;
    private final JwtUtils jwtUtils;
    private final TCUserController tcUserController;

    public TCUserRoutes(UserController userController, JwtUtils jwtUtils, TCUserController tcUserController) {
        this.userController = userController;
        this.jwtUtils = jwtUtils;
        this.tcUserController = tcUserController;
    }

    /**
     * GET
     */
    @GetMapping("")
    @PreAuthorize("hasRole('TCUSER')")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        TCUser data = tcUserController.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }


    /**
     * POST
     */
    @PreAuthorize("hasRole('TCADMIN')")
    @PostMapping("")
    public ResponseEntity<?> register(@RequestHeader("Authorization") final String jwtToken,
                                      @RequestBody TCUserRequest tcUserRequest) {
        UUID adminUUID = UUID.fromString(jwtUtils.getUUIDFromJwtToken(jwtToken));
        return tcUserController.register(tcUserRequest, adminUUID);
    }


    /**
     * PATCH
     */


    /**
     * DELETE
     */
}
