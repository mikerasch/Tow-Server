package edu.uwp.appfactory.tow.routes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.uwp.appfactory.tow.controllers.auth.AuthController;

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
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestHeader("email") final String email,
                                              @RequestHeader("password") final String password) {
        return authController.authenticateUser(email, password);
    }

    //todo: stop using headers, use bodys
    @PostMapping("/registeradmin")
    public ResponseEntity<?> registerAdmin(@RequestHeader("email") final String email,
                                           @RequestHeader("password") final String password,
                                           @RequestHeader("firstname") final String firstname,
                                           @RequestHeader("lastname") final String lastname,
                                           @RequestHeader("phone") final String phone) {
        return authController.registerAdmin(email, password, firstname, lastname, phone)
                ? ResponseEntity.ok("Success")
                : ResponseEntity.status(400).body(null);
    }

    @PostMapping("/registerdriver")
    public ResponseEntity<?> registerDriver(@RequestHeader("email") final String email,
                                            @RequestHeader("password") final String password,
                                            @RequestHeader("firstname") final String firstname,
                                            @RequestHeader("lastname") final String lastname,
                                            @RequestHeader("phone") final String phone) {
        return authController.registerDriver(email, password, firstname, lastname, phone)
                ? ResponseEntity.ok("Success")
                : ResponseEntity.status(400).body(null);
    }

    //    @PostMapping("/registerdriver")
//    public ResponseEntity<?> registerDriver(@RequestBody UserRequest userRequest) {
//        return authController.registerDriver(userRequest.getEmail(), userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname(), userRequest.getPhone())
//                ? ResponseEntity.ok("Success")
//                : ResponseEntity.status(400).body(null);
//    }

    @PostMapping("/registerdispatcher")
    public ResponseEntity<?> registerDispatcher(@RequestHeader("email") final String email,
                                                @RequestHeader("password") final String password,
                                                @RequestHeader("firstname") final String firstname,
                                                @RequestHeader("lastname") final String lastname,
                                                @RequestHeader("precinct") final String precinct,
                                                @RequestHeader("phone") final String phone) {

        return authController
                .registerDispatcher(email, password, firstname, lastname, phone, precinct)
                ? ResponseEntity.ok("Success")
                : ResponseEntity.status(400).body(null);
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verification(@RequestParam("token") final String token) {
        int status = authController.verification(token);
        return switch (status) {
            case 200 -> ResponseEntity.ok("Success");
            case 410 -> ResponseEntity.status(410).body("User already verified");
            case 403 -> ResponseEntity.status(403).body("Link expired, account deleted");
            default -> ResponseEntity.status(404).body("Resource not found");
        };
    }
}