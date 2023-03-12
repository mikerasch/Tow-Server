package edu.uwp.appfactory.tow.controllers.user;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.password.PasswordChange;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UpdateRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Routes which can be used by any role.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Parameterized constructor for creating a new UserController.
     * @param userService useful information for managing all users
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the account based on the UUID of the incoming JWT token.
     * @return user object containing only the necessary information.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER')")
    public ResponseEntity<Users> get(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userService.findById(userDetails));
    }

    /**
     * retrieves the user based on the users email. Only accessible by the admins.
     * perhaps in the future check that the user being deleted is a sub of the admin requesting the delete.
     * @param email email of the user to be deleted.
     * @return returns response message regarding the completion of the delete.
     */
    @DeleteMapping("")
    public ResponseEntity<HttpStatus> delete(@RequestHeader("email") final String email) {
        return userService.deleteByEmail(email) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.status(BAD_REQUEST).body(null);
    }

    /**
     * a Patch method that updates subordinate accounts using the update object for the users info
     * and the jwt to ensure the entity has authorization to edit this users info. whether
     * it is the user or their admin.
     * @param updateRequest
     * @return Users
     */
    @PatchMapping("/update/user")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER') or hasRole('DRIVER')")
    public ResponseEntity<Users> update(@RequestBody UpdateRequest updateRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Users data = userService.updateByUUID(updateRequest,userDetails);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.badRequest().build();
    }
    @PatchMapping("/update/password")
    @PreAuthorize("hasRole('PDADMIN') or hasRole('PDUSER') or hasRole('TCADMIN') or hasRole('TCUSER') or hasRole('DRIVER')")
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody PasswordChange passwordChange, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updatePassword(passwordChange,userDetails);
    }
}
