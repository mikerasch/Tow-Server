package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FailedEmail {

    @Id
    private UUID uuid;

    private UUID user_uuid;
    private String email;
    private String firstname;
    private String lastname;
    private String verify_token;
    private int retries;

    public FailedEmail(String email, UUID user_uuid, String firstname, String lastname, String verify_token) {
        this.email = email;
        this.user_uuid = user_uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.verify_token = verify_token;
    }
}
