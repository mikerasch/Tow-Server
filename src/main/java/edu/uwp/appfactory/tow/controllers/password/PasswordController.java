package edu.uwp.appfactory.tow.controllers.password;

import edu.uwp.appfactory.tow.requestobjects.password.ForgotPassRequest;
import edu.uwp.appfactory.tow.requestobjects.password.ResetPassRequest;
import edu.uwp.appfactory.tow.requestobjects.password.VerifyPassRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Password reset end points.
 */
@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /**
     * Sends an email used to start the password reset process.
     *
     * @param forgotPassRequest user information for password reset
     * @return ResponseEntity HttpStatus, 204 if successful, else 400
     */
    @PatchMapping("/forgot")
    public ResponseEntity<HttpStatus> forgot(@RequestBody ForgotPassRequest forgotPassRequest) {
        return passwordService.forgot(forgotPassRequest)
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(BAD_REQUEST).build();
    }

    /**
     * Route that is hit by the button in the email users receive upon requesting a reset.
     *
     * @param verifyRequest contains the email and token of the requester
     * @return returns the response code to let the user know if it worked or not
     */
    @PatchMapping("/verify")
    public ResponseEntity<HttpStatus> verify(@RequestBody VerifyPassRequest verifyRequest) {
        boolean verify = passwordService.verify(verifyRequest.getEmail(), verifyRequest.getToken());
        if(verify){
            return ResponseEntity.status(NO_CONTENT).build();
        }
        throw new ResponseStatusException(BAD_REQUEST,"Token expired or no associated user");
    }

    /**
     * Route used to update the password.
     *
     * @param resetRequest the email token and new password to be set.
     * @return success or failure code
     */
    @PatchMapping("/reset")
    public ResponseEntity<HttpStatus> reset(@RequestBody ResetPassRequest resetRequest) {
        return passwordService.reset(resetRequest.getEmail(), resetRequest.getToken(), resetRequest.getPassword())
                ? ResponseEntity.status(NO_CONTENT).build()
                : ResponseEntity.status(BAD_REQUEST).build();
    }
}
