package edu.uwp.appfactory.tow.requestObjects.password;

import lombok.Value;

@Value
public class ResetPassRequest {
    String email;
    int token;
    String password;
}
