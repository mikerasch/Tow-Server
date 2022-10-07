package edu.uwp.appfactory.tow.requestObjects.rolerequest;

import lombok.Value;

@Value
public class PDUserRequest {
    String firstname;
    String lastname;
    String email;
    String password;
}
