package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.repositories.DispatcherRepository;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableAutoConfiguration
@Controller
public class LocationController {

    private final DriverRepository driverRepository;
    private final DispatcherRepository dispatcherRepository;
    private final JwtUtils jwtUtils;

    public LocationController(DriverRepository driverRepository, DispatcherRepository dispatcherRepository, JwtUtils jwtUtils) {
        this.driverRepository = driverRepository;
        this.dispatcherRepository = dispatcherRepository;
        this.jwtUtils = jwtUtils;
    }

    public boolean setLocation(float latitude, float longitude, boolean active, String userUUID) {

        System.out.println(userUUID);
        System.out.println(UUID.fromString(userUUID));

        Optional<Driver> driverOptional = driverRepository.findById(UUID.fromString(userUUID));

        System.out.println(driverOptional);

        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            driver.setLongitude(longitude);
            driver.setLatitude(latitude);
            driver.setActive(active);
            driverRepository.save(driver);
            return true;
        } else {
            return false;
        }
    }

    public List<Driver> findByDistance(float latitude, float longitude, int radius) {
        List<Driver> drivers = dispatcherRepository.findByDistance(latitude, longitude, radius);
        if (drivers.size() != 0) {
            return drivers;
        } else {
            return null;
        }
    }
}
