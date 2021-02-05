package edu.uwp.appfactory.tow.routes;

import edu.uwp.appfactory.tow.controllers.auth.AuthController;
import edu.uwp.appfactory.tow.requestObjects.LoginRequest;
import edu.uwp.appfactory.tow.requestObjects.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRoutes {

    private final AuthController authController;

    public AuthRoutes(AuthController authController) {
        this.authController = authController;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") final String jwtToken) {
        String token = authController.refreshToken(jwtToken);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authController.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRequest userRequest) {
        return authController.registerAdmin(userRequest.getEmail(), userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname(), userRequest.getPhone())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }

    @PostMapping("/driver")
    public ResponseEntity<?> registerDriver(@RequestBody UserRequest userRequest) {
        String f = userRequest.getEmail();
        System.out.println(f);
        return authController.registerDriver(userRequest.getEmail(), userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname(), userRequest.getPhone())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }

    @PostMapping("/dispatcher")
    public ResponseEntity<?> registerDispatcher(@RequestBody UserRequest userRequest) {
        return authController.registerDispatcher(userRequest.getEmail(), userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname(), userRequest.getPhone())
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
                : ResponseEntity.status(400).body("Error");
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verification(@RequestParam("token") final String token) {
        int status = authController.verification(token);
        return switch (status) {
            case 200 -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            case 410 -> ResponseEntity.status(410).body("User already verified");
            case 403 -> ResponseEntity.status(403).body("Link expired, account deleted");
            default -> ResponseEntity.status(404).body("Resource not found");
        };
    }
}