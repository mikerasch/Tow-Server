package edu.uwp.appfactory.tow.controllers.superadmin;

import edu.uwp.appfactory.tow.entities.CountDTO;
import edu.uwp.appfactory.tow.entities.UsersDTO;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
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
    private SuperAdminService superAdminService;
    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    //todo add super admin pre auth, don't have it for testing since it is annoying
    @PostMapping
    @ResponseBody
    public ResponseEntity<TestVerifyResponse> registerSuperAdmin(@RequestBody SuperAdminRequest spAdminRequest){
        return superAdminService.register(spAdminRequest);
    }
    @GetMapping("/users/statistics")
    @PreAuthorize("hasRole('SPADMIN')")
    public CountDTO getUserStatistics(){
        return superAdminService.getStatistics();
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UsersDTO usersDTO,
                                                 @RequestParam("oldRole") String oldRole){
        return superAdminService.updateUser(usersDTO,oldRole);
    }
    @GetMapping("/users/allUsers")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<List<UsersDTO>> getAllUsers() {
        return ResponseEntity.ok(superAdminService.getAllUsers());
    }
}
