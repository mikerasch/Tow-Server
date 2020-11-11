package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.queryinterfaces.PDriver;
import edu.uwp.appfactory.tow.repositories.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.util.List;

/**
 * The DispatcherController class communicates between the dispatchers route and the repositories.
 * There are multiple methods contained in this class.
 */
@EnableAutoConfiguration
@Controller
public class DispatcherController {

    private final DispatcherRepository dispatcherRepository;

    /**
     * DispatcherController constructor that news up a dispatch controller and sets the repo equal to the current repo.
     */
    @Autowired
    public DispatcherController(DispatcherRepository dispatcherRepository) {
        this.dispatcherRepository = dispatcherRepository;
    }

    /**
     * This method gathers all of the drivers in the db, that are within the radius of the accident
     */
    public ResponseEntity<?> findAllByDistance(float latitude, float longitude, int radius) {
        List<PDriver> drivers = dispatcherRepository.findAllByDistance(latitude, longitude, radius);

        return ResponseEntity.ok(drivers);
    }
}
