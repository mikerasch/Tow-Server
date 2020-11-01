package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.PasswordController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/password")
public class PasswordRoutes {

    private final PasswordController passwordController;

    public PasswordRoutes(PasswordController passwordController) {
        this.passwordController = passwordController;
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestHeader("email") final String email) {
        return passwordController.forgot(email);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestHeader("email") final String email,
                                    @RequestHeader("token") final int token) {
        return passwordController.verify(email, token);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestHeader("email") final String email,
                                    @RequestHeader("token") final int token,
                                    @RequestHeader("password") final String password) {
        return passwordController.reset(email, token, password);
    }
}
