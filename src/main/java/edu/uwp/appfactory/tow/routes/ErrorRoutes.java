package edu.uwp.appfactory.tow.routes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Basic middleware error handling. Add missing section to application.properties in order to enable this functionality.
 *  The handleError class can be expanded on to include custom error response messages.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/error")
public class ErrorRoutes {

    @GetMapping("/")
    public HttpStatus handleError() {
        return NOT_FOUND;
    }
}
