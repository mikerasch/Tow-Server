package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.DispatcherController;
import edu.uwp.appfactory.tow.controllers.DriverController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/dispatchers")
public class DispatcherRoutes {

    private final DispatcherController dispatcherController;

    @Autowired
    public DispatcherRoutes(DispatcherController dispatcherController) {
        this.dispatcherController = dispatcherController;
    }


    @GetMapping("/accident")
    public ResponseEntity<?> findAllByDistance(@RequestHeader("latitude") final float latitude,
                                               @RequestHeader("longitude") final float longitude,
                                               @RequestHeader("radius") final int radius) {
        return dispatcherController.findAllByDistance(latitude, longitude, radius);
    }


}