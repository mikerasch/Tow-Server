package edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest;

import lombok.Value;

@Value
public class PDUserRequest {
    String firstname;
    String lastname;
    String email;
    String password;
}
