package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class ResetPassRequest {
    String email;
    int token;
    String password;
}
