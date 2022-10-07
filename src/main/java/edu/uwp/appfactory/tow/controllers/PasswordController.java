package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.requestObjects.password.ForgotPassRequest;
import edu.uwp.appfactory.tow.requestObjects.password.ResetPassRequest;
import edu.uwp.appfactory.tow.requestObjects.password.VerifyPassRequest;
import edu.uwp.appfactory.tow.services.roles.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Password reset routes
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * launches an email used to start the password reset process
     * @param forgotPassRequest
     * @return
     */
    @PatchMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotPassRequest forgotPassRequest) {
        return passwordService.forgot(forgotPassRequest)
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(BAD_REQUEST).build();
    }

    /**
     * Route that is hit by the button in the email users receive upon requesting a reset
     * @param verifyRequest contains the email and token of the requester
     * @return returns the response code to let the user know if it worked or not
     */
    @PatchMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyPassRequest verifyRequest) {
        return passwordService.verify(verifyRequest.getEmail(), verifyRequest.getToken())
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(400).body("Token expired or no associated user");
    }

    /**
     * Route used to update the password.
     * @param resetRequest the email token and new password to be set.
     * @return success or failure code
     */
    @PatchMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPassRequest resetRequest) {
        return passwordService.reset(resetRequest.getEmail(), resetRequest.getToken(), resetRequest.getPassword())
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(BAD_REQUEST).build();
    }
}
