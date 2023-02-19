package edu.uwp.appfactory.tow.controllers.driver;

import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.DriverRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @GetMapping()
    public ResponseEntity<Drivers> get(@RequestHeader("Authorization") final String jwtToken){
        return null;
    }

    @PostMapping
    public ResponseEntity<TestVerifyResponse> register(@RequestBody DriverRequest driverRequest){
        return driverService.register(driverRequest);
    }
}
