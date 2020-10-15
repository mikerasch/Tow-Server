package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.DriverController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/drivers")
public class DriverRoutes {

    private final DriverController driverController;

    @Autowired
    public DriverRoutes(DriverController driverController) {
        this.driverController = driverController;
    }

    @GetMapping("/accident")
//    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<?> findAllByDistance(@RequestHeader("latitude") final float latitude,
                                               @RequestHeader("longitude") final float longitude,
                                               @RequestHeader("radius") final int radius){
        return driverController.findAllByDistance(latitude, longitude, radius);
    }

}
