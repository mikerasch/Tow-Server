package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class FailedEmail implements Serializable {

    @Id
    UUID uuid;
    UUID user_uuid;
    String email;
    String firstname;
    String lastname;
    String verify_token;
    int retries;
}
