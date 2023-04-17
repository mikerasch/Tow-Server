package edu.uwp.appfactory.tow.controllers.policedepartment;

import edu.uwp.appfactory.tow.controllers.auth.AuthController;
import edu.uwp.appfactory.tow.controllers.user.UserController;
import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.PDUserRequest;
import edu.uwp.appfactory.tow.responseObjects.PDUAuthResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Used to create police department user accounts requiring a PD admin to register.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pdusers")
public class PDUserController {
    private final PDUserService pdUserService;

    public PDUserController(PDUserService pdUserService) {
        this.pdUserService = pdUserService;
    }

    /**
     * Returns user based off the UUID from the JWT token. Only usable currently by TC users.
     * For all other authentication options view the link below.
     *
     * @return UUID information if successful, 400 otherwise
     * @see UserController
     * @see AuthController#refreshToken(String)
     */
    @GetMapping("")
    @PreAuthorize("hasRole('PDUSER')")
    public ResponseEntity<PDUser> get(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        PDUser data = pdUserService.get(userDetails);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Registers a new PD user using the PD admins jwt for further authentication.
     *
     * @param pdUserRequest - contains the information of a user
     * @return information relating to the newly created PD user, 400 otherwise
     * @see AuthController#refreshToken(String)
     */
    @PostMapping("")
    @PreAuthorize("hasRole('PDADMIN')")
    public ResponseEntity<PDUAuthResponse> register(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody PDUserRequest pdUserRequest) {
        return pdUserService.register(pdUserRequest, userDetails);
    }
}
