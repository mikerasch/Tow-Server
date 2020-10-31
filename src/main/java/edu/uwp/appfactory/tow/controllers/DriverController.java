/**
 *  The DriverController class communicates between the dispatchers route and the repositories.
 *  There are multiple methods contained in this class.
 */

package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.data.PDriver;
import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@EnableAutoConfiguration
@Controller
public class DriverController {


    /**
     * Newing DriverRepository object.
     */
    private final DriverRepository driverRepository;
    private final JwtUtils jwtUtils;
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * Driver controller constructor matching the local repo newly created,
     * to the actual driverRepository.
     * @param driverRepository sends in current repo to set equal to the local repo
     * @param jwtUtils
     */
    @Autowired
    public DriverController(DriverRepository driverRepository, JwtUtils jwtUtils) {
        this.driverRepository = driverRepository;
        this.jwtUtils = jwtUtils;
    }

    /**
     * This method gathers all of the drivers in the db, that are within the radius of the accident
     * @param latitude the lat of the accident site
     * @param longitude the long o the accident site
     * @param radius the max radius that the dispathcer chooses to return within
     * @return returns the list available drivers.
     */
    public ResponseEntity<?> findAllByDistance(float latitude, float longitude, int radius) {
        List<PDriver> drivers = driverRepository.findAllByDistance(latitude, longitude, radius);

        return ResponseEntity.ok(drivers);
    }

    /**
     * work in progress setLocation that
     * @param latitude
     */
    public ResponseEntity<?> setLocation(float latitude, float longitude, boolean active, String authorization) {
        try {
            logger.debug(authorization);
            String UUID = jwtUtils.getUUIDFromJwtToken(authorization);
            Optional<Driver> driverOptional = driverRepository.findByUUID(UUID);
            if (driverOptional.isEmpty()) {
                return ResponseEntity
                        .status(500)
                        .body(new MessageResponse("Not successful!"));
            }

            Driver driver = driverOptional.get();

            driver.setLongitude(longitude);
            driver.setLatitude(latitude);
            driver.setActive(active);
            driverRepository.save(driver);

            return ResponseEntity
                    .status(200)
                    .body(new MessageResponse("Great successful!"));

        } catch (ConstraintViolationException e) {
            return ResponseEntity.status(498).body("Invalid Entries: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(499).body("Error: " + e.getMessage());
        }
    }

}
