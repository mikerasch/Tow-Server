package edu.uwp.appfactory.tow.controllers.superAdmin;

import edu.uwp.appfactory.tow.entities.UsersDTO;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/tcadmins/count")
    @PreAuthorize("hasRole('SPADMIN')")
    public long getTCAdminCount(@RequestHeader("Authorization") final String jwtToken){
        return superAdminService.getTCAdminCount();
    }
    @GetMapping("/pdadmins/count")
    @PreAuthorize("hasRole('SPADMIN')")
    public long getPDAdminCount(@RequestHeader("Authorization") final String jwtToken){
        return superAdminService.getPDAdminCount();
    }
    @GetMapping("/users/count")
    @PreAuthorize("hasRole('SPADMIN')")
    public long getUserCount(@RequestHeader("Authorization") final String jwtToken){
        return superAdminService.getUserCount();
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('SPADMIN')")
    public List<UsersDTO> getAllUsers(@RequestHeader("Authorization") final String jwtToken){
        return superAdminService.getAllUsers();
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<HttpStatus> updateUser(@RequestHeader("Authorization") final String jwtToken,
                                                 @RequestBody UsersDTO usersDTO,
                                                 @RequestParam("oldRole") String oldRole){
        return superAdminService.updateUser(usersDTO,oldRole);
    }
}
