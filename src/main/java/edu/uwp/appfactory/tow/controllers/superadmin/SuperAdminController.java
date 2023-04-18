package edu.uwp.appfactory.tow.controllers.superadmin;

import edu.uwp.appfactory.tow.requestobjects.statistics.CountDTO;
import edu.uwp.appfactory.tow.requestobjects.users.UsersDTO;
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

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasRole('SPADMIN')")
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

    @DeleteMapping("/delete/user")
    @PreAuthorize("hasRole('SPADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@RequestHeader String email) {
        return superAdminService.deleteUser(email);
    }

}
