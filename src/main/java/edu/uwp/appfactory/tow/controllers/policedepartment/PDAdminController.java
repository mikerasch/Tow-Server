package edu.uwp.appfactory.tow.controllers.policedepartment;

import edu.uwp.appfactory.tow.responseobjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.PDAdminRequest;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Responsible for registering and retrieving PD admin information.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pdadmins")
public class PDAdminController {
    private final PDAdminService pdAdminService;

    public PDAdminController(PDAdminService pdAdminService) {
        this.pdAdminService = pdAdminService;
    }

    /**
     * This route retrieves information about a requested police department admin.
     *
     * @return returns an object that contains only the necessary information about the admin.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('PDADMIN')")
    public ResponseEntity<PDAdmin> get(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        PDAdmin data = pdAdminService.get(userDetails);
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    /**
     * Registers a new admin in the system.
     *
     * @param pdAdminRequest - police department admin account information
     * @return verification token if successful, else 400
     */
    @PostMapping("")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<TestVerifyResponse> register(@RequestBody PDAdminRequest pdAdminRequest) {
        return pdAdminService.register(pdAdminRequest);
    }
}
