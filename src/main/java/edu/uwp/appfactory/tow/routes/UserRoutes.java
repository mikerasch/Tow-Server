package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.queryinterfaces.UpdateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserRoutes {

    private final UserController userController;
    private final JwtUtils jwtUtils;

    public UserRoutes(UserController userController, JwtUtils jwtUtils) {
        this.userController = userController;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('DRIVER') or hasRole('DISPATCHER')")
    public ResponseEntity<?> get(@RequestHeader("email") final String email) {
        Users data = userController.findByEmail(email);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestHeader("email") final String email) {
        return userController.deleteByEmail(email) ? ResponseEntity.ok("Success") : ResponseEntity.status(400).body(null);
    }

    //todo: JWT AUTH
    @PatchMapping(value = "")
    @PreAuthorize("hasRole('DRIVER') or hasRole('DISPATCHER')")
    public ResponseEntity<?> update(@RequestHeader("Authorization") final String jwtToken,
                                    @RequestBody UpdateUser userNew) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        Users data = userController.updateByUUID(userUUID, userNew);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }
}
