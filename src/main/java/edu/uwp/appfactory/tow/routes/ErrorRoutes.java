package edu.uwp.appfactory.tow.routes;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/error")
public class ErrorRoutes {

    @GetMapping("/")
    public String handleError() {
        return "Error 404 not found BROOOOOO";
    }
}
