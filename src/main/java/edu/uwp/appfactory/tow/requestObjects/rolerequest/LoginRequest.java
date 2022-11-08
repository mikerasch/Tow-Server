package edu.uwp.appfactory.tow.requestObjects.rolerequest;

import lombok.Value;

@Value
public class LoginRequest {
    String email;
    String password;
}
