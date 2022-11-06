package edu.uwp.appfactory.tow.controllers.driver;

import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.DriverRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private DriverService driverService;
    // to
    @GetMapping()
    public ResponseEntity<Drivers> get(@RequestHeader("Authorization") final String jwtToken){
        return null;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<List<Drivers>> getAll(@RequestHeader("Authorization") final String jwtToken){
        return ResponseEntity.ok(new ArrayList<>());
    }
    @PostMapping()
    public ResponseEntity<TestVerifyResponse> register(@RequestBody DriverRequest driverRequest){
        return driverService.register(driverRequest);
    }
}
