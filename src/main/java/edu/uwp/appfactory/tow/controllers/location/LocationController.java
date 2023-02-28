package edu.uwp.appfactory.tow.controllers.location;

import edu.uwp.appfactory.tow.controllers.auth.AuthController;
import edu.uwp.appfactory.tow.requestobjects.location.Coordinates;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     * @param setRequest - location data of TCUSER
     * @return 204 if successful, else 400
     * @see AuthController#refreshToken(String)
     */
    @PreAuthorize("hasRole('TCUSER')")
    @PostMapping("/update/location")
    public ResponseEntity<HttpStatus> updateLocation(@RequestBody Coordinates setRequest, @AuthenticationPrincipal UserDetailsImpl user) {
        return locationService.setLocation(setRequest.getLatitude(), setRequest.getLongitude(), user)
                ? ResponseEntity.status(NO_CONTENT).body(null)
                : ResponseEntity.status(BAD_REQUEST).build();
    }

    @PreAuthorize("hasRole('TCUSER')")
    @PostMapping("/update/active")
    public ResponseEntity<HttpStatus> updateActiveStatus(@RequestHeader("status") String status, @AuthenticationPrincipal UserDetailsImpl user) {
        return locationService.updateActiveStatus(Boolean.parseBoolean(status),user);
    }
}