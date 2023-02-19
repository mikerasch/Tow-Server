package edu.uwp.appfactory.tow.controllers.requestObjects.password;

import lombok.Value;

@Value
public class ResetPassRequest {
    String email;
    int token;
    String password;
}
