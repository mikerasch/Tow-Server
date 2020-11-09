/**
 * The DispatcherController class communicates between the dispatchers route and the repositories.
 * There are multiple methods contained in this class.
 */

package edu.uwp.appfactory.tow.controllers;
import edu.uwp.appfactory.tow.queryinterfaces.PDriver;
import edu.uwp.appfactory.tow.repositories.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;


@EnableAutoConfiguration
@Controller
public class DispatcherController {

    /**
     * Creating a new dispatcher repository object.
     */
    private final DispatcherRepository dispatcherRepository;

    /**
     * DispatcherController constructor that news up a dispatch
     * controller and sets the repo equal to the current repo.
     * @param dispatcherRepository set the dispatchrepo to match the current repo
     */
    @Autowired
    public DispatcherController(DispatcherRepository dispatcherRepository) {
        this.dispatcherRepository = dispatcherRepository;
    }

    /**
     * This method gathers all of the drivers in the db, that are within the radius of the accident
     * @param latitude the lat of the accident site
     * @param longitude the long o the accident site
     * @param radius the max radius that the dispathcer chooses to return within
     * @return returns the list available drivers.
     */
    public ResponseEntity<?> findAllByDistance(float latitude, float longitude, int radius) {
        List<PDriver> drivers = dispatcherRepository.findAllByDistance(latitude, longitude, radius);

        return ResponseEntity.ok(drivers);
    }

}
