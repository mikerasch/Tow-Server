/**
 *  The DriverController class communicates between the dispatchers route and the repositories.
 *  There are multiple methods contained in this class.
 */

package edu.uwp.appfactory.tow.controllers;
import edu.uwp.appfactory.tow.data.PDriver;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.util.List;


@EnableAutoConfiguration
@Controller
public class DriverController {


    /**
     * Newing DriverRepository object.
     */
    private final DriverRepository driverRepository;

    /**
     * Driver controller constructor matching the local repo newly created,
     * to the actual driverRepository.
     * @param driverRepository sends in current repo to set equal to the local repo
     */
    @Autowired
    public DriverController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;

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


}
