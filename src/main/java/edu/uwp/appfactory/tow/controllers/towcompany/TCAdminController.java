package edu.uwp.appfactory.tow.controllers.towcompany;

import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.TCAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TCAdminResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * Registers and retrieves PD admin information.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcadmins")
public class TCAdminController {
    private final TCAdminService tcAdminService;

    /**
     * Parameterized constructor for creating a new TCAdminController.
     * @param tcAdminService - service to access/provide useful tow company ADMIN logic
     */
    public TCAdminController(TCAdminService tcAdminService) {
        this.tcAdminService = tcAdminService;
    }

    /**
     * Retrieves information about a requested tow truck admin.
     * @return returns an object that contains only the necessary information about the admin.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('TCADMIN')")
    public ResponseEntity<TCAdminResponse> get(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(tcAdminService.get(userDetails));
    }

    //todo safeguard in the future
    /**
     * Registers a new tc admin.
     * @param tcAdminRequest - tow company admin account information
     * @return token of newly created tc admin, 400 otherwise
     */
    @PostMapping()
    @ResponseBody
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<TestVerifyResponse> register(@RequestBody TCAdminRequest tcAdminRequest) {
        return tcAdminService.register(tcAdminRequest);

    }
    //todo add Patch and Delete
}
