package edu.uwp.appfactory.tow.controllers.towcompany;

import edu.uwp.appfactory.tow.controllers.user.UserController;
import edu.uwp.appfactory.tow.responseobjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.TCUserRequest;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Register and maintain tc user accounts. Only TC Admins can access these routes.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcusers")
public class TCUserController {
    private final TCUserService tcUserService;

    /**
     * Parameterized constructor for creating a new TCUserController.
     * @param tcUserService - service to handle registration and maintenance of tow company users.
     */
    public TCUserController(TCUserService tcUserService) {
        this.tcUserService = tcUserService;
    }

    /**
     * Retrieves user based off the UUID from the JWT token.
     * Only usable by tc users, for other auth options, see below.
     * @return UUID information of TC user, else 400
     * @see UserController
     */
    @GetMapping()
    @PreAuthorize("hasRole('TCUSER')")
    public ResponseEntity<TCUser> get(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        TCUser data = tcUserService.get(userDetails);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves all user based off the UUID from the JWT token.
     * @return list of all tow company users, else 400
     * @see UserController
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('TCADMIN')")
    public ResponseEntity<List<TCUser>> getAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(tcUserService.getAll(userDetails));
    }

    /**
     * Registers a new tow company user.
     * @param tcUserRequest contains information of new tow company user to be added
     * @return new tow company users token, else 400
     */
    @PreAuthorize("hasRole('TCADMIN')")
    @PostMapping()
    public ResponseEntity<TestVerifyResponse> register(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @RequestBody TCUserRequest tcUserRequest) {
        return tcUserService.register(tcUserRequest, userDetails);
    }
}
