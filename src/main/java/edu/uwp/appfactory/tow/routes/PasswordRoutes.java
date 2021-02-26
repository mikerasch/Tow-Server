package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.PasswordController;
import edu.uwp.appfactory.tow.requestObjects.password.ForgotPassRequest;
import edu.uwp.appfactory.tow.requestObjects.password.ResetPassRequest;
import edu.uwp.appfactory.tow.requestObjects.password.VerifyPassRequest;
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
    public ResponseEntity<?> forgot(@RequestBody ForgotPassRequest forgotPassRequest) {
        return passwordController.forgot(forgotPassRequest)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }

    @PatchMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyPassRequest verifyRequest) {
        return passwordController.verify(verifyRequest.getEmail(), verifyRequest.getToken())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Token expired or no associated user");
    }

    @PatchMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPassRequest resetRequest) {
        return passwordController.reset(resetRequest.getEmail(), resetRequest.getToken(), resetRequest.getPassword())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }
}
