package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class LoginRequest {
    String email;
    String password;
    String platform;
}
