package edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest;

import lombok.Value;

@Value
public class TCUserRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
    float latitude;
    float longitude;
    int radius;
}
