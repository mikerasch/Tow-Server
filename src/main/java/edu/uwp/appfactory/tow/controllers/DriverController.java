package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.data.pogoDriver;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;


@EnableAutoConfiguration
@Controller
public class DriverController {

    private final UsersRepository usersRepository;

    private final DriverRepository driverRepository;

    @Autowired
    public DriverController(UsersRepository usersRepository, DriverRepository driverRepository) {
        this.usersRepository = usersRepository;
        this.driverRepository = driverRepository;
    }

    public ResponseEntity<?> findAllByDistance(float latitude, float longitude, int radius) {
        List<pogoDriver> drivers = driverRepository.findAllByDistance(latitude, longitude, radius);

        return ResponseEntity.ok(drivers);
    }

}
