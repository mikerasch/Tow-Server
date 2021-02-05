package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.controllers.LocationController;
import edu.uwp.appfactory.tow.requestObjects.DriversRequest;
import edu.uwp.appfactory.tow.requestObjects.SetLocationRequest;
import org.springframework.http.HttpStatus;
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
        System.out.println("lat: " + setRequest.getLatitude() + " long: " + setRequest.getLongitude() + " active: " + setRequest.isActive());
        return locationController.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }

    //todo: patch doesnt really align, but need in order to send body data
    //todo: it needs to be 200 or something even if no drivers are returned
    @PreAuthorize("hasRole('DISPATCHER')")
    @PatchMapping("/driver-locations")
    public ResponseEntity<?> getLocations(@RequestBody DriversRequest driversRequest) {
        List<?> data = locationController.findByDistance(driversRequest.getLatitude(), driversRequest.getLongitude(), driversRequest.getRadius());
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }
}