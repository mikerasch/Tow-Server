package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.DriverController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The route communicates between the Tow truck drivers device and the service.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/drivers")
public class DriverRoutes {

    private final DriverController driverController;

    @Autowired
    public DriverRoutes(DriverController driverController) {
        this.driverController = driverController;
    }
}
