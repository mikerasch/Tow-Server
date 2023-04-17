package edu.uwp.appfactory.tow.controllers.driver;

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

    /**
     * Registers a new driver using the provided driver request data.
     *
     * @param driverRequest The driver request object containing the necessary driver information.
     * @return A ResponseEntity with a TestVerifyResponse object containing a message indicating the success of the registration.
     */
    @PostMapping
    public ResponseEntity<TestVerifyResponse> register(@RequestBody DriverRequest driverRequest){
        return driverService.register(driverRequest);
    }

    /**
     * Updates the location of the authenticated driver.
     *
     * @param coordinates The new location of the driver.
     * @param user The authenticated user details.
     * @return A ResponseEntity with an HTTP status indicating the success of the update.
     */
    @PostMapping("/update/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<HttpStatus> updateLocation(@RequestBody Coordinates coordinates, @AuthenticationPrincipal UserDetailsImpl user) {
        return driverService.updateLocation(coordinates,user);
    }

    /**
     * Finds available tow truck drivers within the specified radius of the authenticated driver's location.
     *
     * @param radius The radius within which to search for available tow truck drivers.
     * @param user The authenticated user details.
     * @return A ResponseEntity with an HTTP status indicating the success of the search.
     */
    @PostMapping("/find/towtrucks")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<HttpStatus> findTowTruckDrivers(@RequestHeader("radius") int radius, @AuthenticationPrincipal UserDetailsImpl user) {
        return driverService.findTowTruckDrivers(radius, user);
    }
}
