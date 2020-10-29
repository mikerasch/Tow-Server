/**
 * The route communicates between the Tow truck drivers device and the service.
 */

package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.DriverController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/dispatchers")
public class DriverRoutes {

    private final DriverController driverController;

    @Autowired
    public DriverRoutes(DriverController driverController) {
        this.driverController = driverController;
    }


    @GetMapping("/accident")
    public ResponseEntity<?> findAllByDistance(@RequestHeader("latitude") final float latitude,
                                               @RequestHeader("longitude") final float longitude,
                                               @RequestHeader("radius") final int radius) {
        return driverController.findAllByDistance(latitude, longitude, radius);
    }

    /**
     * A method that receives the drivers UTM data, name, and  via the Drivers app
     *
     * @param latitude
     * @param longitude
     * @param active
     * @return
     */
    @PostMapping("/location")
    public String getLocation(@RequestHeader("latitude") final float latitude,
                              @RequestHeader("longitude") final float longitude,
                              @RequestHeader("active") final int active,
                              @RequestHeader("uuid") final char uuid
    ) {


        return "!!!!OK!!!";
    }


}
