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

/**
 * End point for handling all requests related to location of driver and tow-truck.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final JwtUtils jwtUtils;

    /**
     * Parametrized constructor for creating a new LocationController instance.
     * @param locationService - service layer for accessing location data
     * @param jwtUtils handling management of JWT tokens for security
     */
    public LocationController(LocationService locationService, JwtUtils jwtUtils) {
        this.locationService = locationService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Handling setting location of a TCUSER.
     * PreAuthorization requires request be made by an active TCUSER and jwtToken.
     * @param jwtToken - JWT token, must be active, else request a refresh
     * @param setRequest - location data of TCUSER
     * @return 204 if successful, else 400
     * @see edu.uwp.appfactory.tow.controllers.AuthController#refreshToken(String)
     */
    @PreAuthorize("hasRole('TCUSER')")
    @PatchMapping("/my-location")
    public ResponseEntity<?> setLocation(@RequestHeader("Authorization") final String jwtToken,
                                         @RequestBody TCULocationRequest setRequest) {
        String userUUID = jwtUtils.getUUIDFromJwtToken(jwtToken);
        return locationService.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), setRequest.isActive(), userUUID)
                ? ResponseEntity.status(NO_CONTENT).body(null)
                : ResponseEntity.status(BAD_REQUEST).build();
    }

    //todo: patch doesn't really align, but need in order to send body data
    //todo: it needs to be 200 or something even if no drivers are returned
    //todo: previous thoughts: @PatchMapping("/driver-locations")

    /**
     * Retrieves the list of tow truck drivers who are within range and are active.
     * @param driversRequest - police department location data
     * @return - list of closest and available tow truck drivers, else 400
     */
    @PreAuthorize("hasRole('PDUSER')")
    @GetMapping("/driver-locations")
    public ResponseEntity<?> getLocations(@RequestBody DriversRadiusRequest driversRequest) {
        List<?> data = locationService.findByDistance(driversRequest.getLatitude(), driversRequest.getLongitude(), driversRequest.getRadius());
        if(data.isEmpty()){
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        return ResponseEntity.ok(data);
    }
}