package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.LocationController;
import edu.uwp.appfactory.tow.requestObjects.DriversRequest;
import edu.uwp.appfactory.tow.requestObjects.SetLocationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/locations")
public class LocationRoutes {

    private final LocationController locationController;
    private final JwtUtils jwtUtils;

    public LocationRoutes(LocationController locationController, JwtUtils jwtUtils) {
        this.locationController = locationController;
        this.jwtUtils = jwtUtils;
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PatchMapping("/my-location")
    public ResponseEntity<?> setLocation(@RequestHeader("Authorization") final String jwtToken,
                                         @RequestBody SetLocationRequest setRequest) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        return locationController.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
                ? ResponseEntity.ok("Success")
                : ResponseEntity.status(400).body("Error");
    }

    @PreAuthorize("hasRole('DISPATCHER')")
    @GetMapping("/driver-locations")
    public ResponseEntity<?> getLocations(@RequestHeader("Authorization") final String jwtToken,
                                          @RequestBody DriversRequest driversRequest) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        List<?> data = locationController.findByDistance(driversRequest.getLatitude(), driversRequest.getLongitude(), driversRequest.getRadius());
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }
}