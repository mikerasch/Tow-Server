package edu.uwp.appfactory.tow.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/error")
public class ErrorRoutes {



    @PostMapping("/general")
    public String handleError() {
        return "error";
    }

}
