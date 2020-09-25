package edu.uwp.appfactory.tow.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.Users;
import edu.uwp.appfactory.tow.controllers.admin.AdminController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admins")
public class AdminRoutes {

    private final AdminController adminController;

    @Autowired
    public AdminRoutes(AdminController adminController){
        this.adminController = adminController;
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        return adminController.findAll();
    }

    @GetMapping(value = "", headers = "id")
    public ResponseEntity<?> getAdminId(@RequestHeader("id") long id){
        return adminController.findByID(id);
    }

    @GetMapping(value = "", headers = "email")
    public ResponseEntity<?> getAdminEmail(@RequestHeader("email") String email){
        return adminController.findByEmail(email);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Users admin){
        admin.setRoles("ROLE_ADMIN");
        return adminController.create(admin);
    }

    @DeleteMapping(value = "", headers = "id")
    public ResponseEntity<?> delete(@RequestHeader("id") long id){
        return adminController.delete(id);
    }

    @PatchMapping(value = "", headers = "id")
    public ResponseEntity<?> update(@RequestHeader("id") long id, @RequestBody Users admin){
        return adminController.update(id, admin);
    }
}
