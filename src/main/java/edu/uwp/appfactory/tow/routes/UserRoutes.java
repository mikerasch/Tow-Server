package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestObjects.UpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    public ResponseEntity<?> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        Users data = userController.findById(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestHeader("email") final String email) {
        return userController.deleteByEmail(email) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(400).body(null);
    }

    //todo: JWT AUTH
    @PatchMapping(value = "")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    public ResponseEntity<?> update(@RequestHeader("Authorization") final String jwtToken,
                                    @RequestBody UpdateRequest updateRequest) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        Users data = userController.updateByUUID(UUID.fromString(userId), updateRequest.getFirstname(), updateRequest.getLastname(), updateRequest.getEmail(), updateRequest.getPhone());
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }
}
