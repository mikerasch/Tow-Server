package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.requestObjects.location.DriversRadiusRequest;
import edu.uwp.appfactory.tow.requestObjects.location.TCULocationRequest;
import edu.uwp.appfactory.tow.services.locator.LocationService;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final JwtUtils jwtUtils;

    public LocationController(LocationService locationService, JwtUtils jwtUtils) {
        this.locationService = locationService;
        this.jwtUtils = jwtUtils;
    }

    @PreAuthorize("hasRole('TCUSER')")
    @PatchMapping("/my-location")
    public ResponseEntity<?> setLocation(@RequestHeader("Authorization") final String jwtToken,
                                         @RequestBody TCULocationRequest setRequest) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        System.out.println("lat: " + setRequest.getLatitude() + " long: " + setRequest.getLongitude() + " active: " + setRequest.isActive());
        return locationService.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
                ? ResponseEntity.status(NO_CONTENT).body(null)
                : ResponseEntity.status(BAD_REQUEST).build();
    }

//    //todo: patch doesnt really align, but need in order to send body data
//    //todo: it needs to be 200 or something even if no drivers are returned
    @PreAuthorize("hasRole('PDUSER')")
//    @PatchMapping("/driver-locations")
    @GetMapping("/driver-locations")
    public ResponseEntity<?> getLocations(@RequestBody DriversRadiusRequest driversRequest) {
        List<?> data = locationService.findByDistance(driversRequest.getLatitude(), driversRequest.getLongitude(), driversRequest.getRadius());
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }
}