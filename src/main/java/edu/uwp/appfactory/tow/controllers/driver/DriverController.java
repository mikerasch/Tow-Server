package edu.uwp.appfactory.tow.controllers.driver;

import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.requestobjects.location.Coordinates;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.DriverRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping()
    public ResponseEntity<Drivers> get(@RequestHeader("Authorization") final String jwtToken){
        return null;
    }

    @PostMapping
    public ResponseEntity<TestVerifyResponse> register(@RequestBody DriverRequest driverRequest){
        return driverService.register(driverRequest);
    }
    @PostMapping("/update/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<HttpStatus> updateLocation(@RequestBody Coordinates coordinates, @AuthenticationPrincipal UserDetailsImpl user) {
        return driverService.updateLocation(coordinates,user);
    }

    @PostMapping("/find/towtrucks")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<HttpStatus> findTowTruckDrivers(@RequestHeader("radius") int radius, @AuthenticationPrincipal UserDetailsImpl user) {
        return driverService.findTowTruckDrivers(radius, user);
    }
}
