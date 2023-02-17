package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.Value;

@Value
public class LoginRequest {
    String email;
    String password;
}
