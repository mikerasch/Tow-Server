package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.PasswordController;
import edu.uwp.appfactory.tow.requestObjects.PasswordForgotRequest;
import edu.uwp.appfactory.tow.requestObjects.PasswordResetRequest;
import edu.uwp.appfactory.tow.requestObjects.PasswordVerifyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/password")
public class PasswordRoutes {

    private final PasswordController passwordController;

    public PasswordRoutes(PasswordController passwordController) {
        this.passwordController = passwordController;
    }

    @PatchMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody PasswordForgotRequest forgotRequest) {
        return passwordController.forgot(forgotRequest.getEmail())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }

    @PatchMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody PasswordVerifyRequest verifyRequest) {
        return passwordController.verify(verifyRequest.getEmail(), verifyRequest.getToken())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Token expired or no associated user");
    }

    @PatchMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody PasswordResetRequest resetRequest) {
        return passwordController.reset(resetRequest.getEmail(), resetRequest.getToken(), resetRequest.getPassword())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }
}
