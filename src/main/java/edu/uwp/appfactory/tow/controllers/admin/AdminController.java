package edu.uwp.appfactory.tow.controllers.admin;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.Users;
import edu.uwp.appfactory.tow.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;


import java.util.Optional;

@EnableAutoConfiguration
@Controller
public class AdminController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;


    @Autowired
    public AdminController(AdminRepository adminRepository, PasswordEncoder encoder){
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(adminRepository.findAllAdmin());
    }

    public ResponseEntity<?> findByID(long id){
        Optional admin =  adminRepository.findById(id);
        if (admin.isPresent())
            return ResponseEntity.ok(admin.get());
        else
            return ResponseEntity.status(400).body("No admin found");
    }

    public ResponseEntity<?> findByEmail(String email){
        Optional admin =  adminRepository.findByEmail(email);
        if (admin.isPresent())
            return ResponseEntity.ok(admin.get());
        else
            return ResponseEntity.status(400).body("No admin found");
    }

    public ResponseEntity<?> delete(long id){
        Optional admin = adminRepository.findById(id);
        if (admin.isPresent()){
            adminRepository.deleteById(id);
            return ResponseEntity.ok(admin.get());
        } else {
            return ResponseEntity.status(400).body("No admin found");
        }
    }

    public ResponseEntity<?> update(long id, Users admin){
        Optional oldAdmin = adminRepository.findById(id);
        if (oldAdmin.isPresent()){
            admin.setPassword(encoder.encode(admin.getPassword()));
            admin.setId(id);
            adminRepository.save(admin);
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(400).body("No admin found");
        }
    }

    public ResponseEntity<?> create(Users admin){
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return ResponseEntity.status(201).body(admin);
    }



}
