package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.DispatcherController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/dispatchers")
public class DispatcherRoutes {

    private final DispatcherController dispatcherController;

    @Autowired
    public DispatcherRoutes(DispatcherController dispatcherController) {
        this.dispatcherController = dispatcherController;
    }
}