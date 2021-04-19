package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.UserController;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestObjects.UpdateRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * This class contains the routes that arent role specific.
 */
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

    /**
     * Retrieves the account based on the UUID of the incoming JWT token.
     * @param jwtToken token of the requesting entity.
     * @return user object containing only the necessary information.
     */
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

    /**
     * retrieves the user based on the users email. Only accessible by the admins.
     * perhaps in the future check that the user being deleted is a sub of the admin requesting the delete.
     * @param email email of the user to be deleted.
     * @return returns response message regarding the completion of the delete.
     */
    @DeleteMapping("")
    public ResponseEntity<?> delete(@RequestHeader("email") final String email) {
        return userController.deleteByEmail(email) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(400).body(null);
    }

    /**
     * a Patch method that updates subordinate accounts using the update object for the users info
     * and the jwt to ensure the entity has authorization to edit this users info. whether
     * it is the user or their admin.
     * @param jwtToken
     * @param updateRequest
     * @return
     */
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
