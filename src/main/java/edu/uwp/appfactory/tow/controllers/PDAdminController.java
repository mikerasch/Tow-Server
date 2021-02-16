package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

@EnableAutoConfiguration
@Controller
public class PDAdminController {

    private final DriverRepository driverRepository;

    @Autowired
    public PDAdminController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }
}
