package edu.uwp.appfactory.tow.WebSecurityConfig.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * JWT response class
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String role;
    private String phone;

    /**
     * constructor for a JWT response, used mostly with login
     */
    public JwtResponse(String accessToken, UUID id, String username, String email, String
            firstname, String lastname, String role, String phone) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.phone = phone;
    }
}
