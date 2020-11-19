package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 *  The DriverController class communicates between the dispatchers route and the repositories.
 *  There are multiple methods contained in this class.
 */
@EnableAutoConfiguration
@Controller
public class DriverController {

    private final DriverRepository driverRepository;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Driver controller constructor matching the local repo newly created to the actual driverRepository.
     */
    @Autowired
    public DriverController(DriverRepository driverRepository, JwtUtils jwtUtils) {
        this.driverRepository = driverRepository;
        this.jwtUtils = jwtUtils;
    }

    /**
     * work in progress setLocation that
     */
    public ResponseEntity<?> setLocation(float latitude, float longitude, String truck, boolean active, String authorization) {
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
            driver.setTruck(truck);
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
