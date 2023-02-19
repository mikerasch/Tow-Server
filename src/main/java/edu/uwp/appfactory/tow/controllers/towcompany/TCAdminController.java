package edu.uwp.appfactory.tow.controllers.towcompany;

import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.TCAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TCAdminResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
/**
 * Registers and retrieves PD admin information.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tcadmins")
public class TCAdminController {
    private final JwtUtils jwtUtils;
    private final TCAdminService tcAdminService;

    /**
     * Parameterized constructor for creating a new TCAdminController.
     * @param jwtUtils - handling management of JWT tokens for security
     * @param tcAdminService - service to access/provide useful tow company ADMIN logic
     */
    public TCAdminController(JwtUtils jwtUtils, TCAdminService tcAdminService) {
        this.jwtUtils = jwtUtils;
        this.tcAdminService = tcAdminService;
    }

    /**
     * Retrieves information about a requested tow truck admin.
     * @param jwtToken the PDAdmins token so we can locate using the UUID
     * @return returns an object that contains only the necessary information about the admin.
     */
    @GetMapping("")
    @PreAuthorize("hasRole('TCADMIN')")
    public ResponseEntity<TCAdminResponse> get(@RequestHeader("Authorization") final String jwtToken) {
        String userId = jwtUtils.getUUIDFromJwtToken(jwtToken);
        TCAdminResponse data = tcAdminService.get(UUID.fromString(userId));
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    //todo safeguard in the future
    /**
     * Registers a new tc admin.
     * @param tcAdminRequest - tow company admin account information
     * @return token of newly created tc admin, 400 otherwise
     */
    @PostMapping()
    @ResponseBody
    public ResponseEntity<TestVerifyResponse> register(@RequestBody TCAdminRequest tcAdminRequest) {
        return tcAdminService.register(tcAdminRequest);

    }
    //todo add Patch and Delete
}
