package edu.uwp.appfactory.tow.controllers.superAdmin;

import edu.uwp.appfactory.tow.entities.CountDTO;
import edu.uwp.appfactory.tow.entities.UsersDTO;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/spadmins")
public class SuperAdminController {
    @Autowired
    private SuperAdminService superAdminService;

    //todo add super admin pre auth, don't have it for testing since it is annoying
    @PostMapping
    @ResponseBody
    public ResponseEntity<TestVerifyResponse> registerSuperAdmin(@RequestBody SuperAdminRequest spAdminRequest){
        return superAdminService.register(spAdminRequest);
    }
    @GetMapping("/users/statistics")
    @PreAuthorize("hasRole('SPADMIN')")
    public CountDTO getUserStatistics(@RequestHeader("Authorization") final String jwtToken){
        return superAdminService.getStatistics();
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<HttpStatus> updateUser(@RequestHeader("Authorization") final String jwtToken,
                                                 @RequestBody UsersDTO usersDTO,
                                                 @RequestParam("oldRole") String oldRole){
        return superAdminService.updateUser(usersDTO,oldRole);
    }
}
