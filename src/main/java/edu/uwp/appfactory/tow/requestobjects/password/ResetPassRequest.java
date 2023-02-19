package edu.uwp.appfactory.tow.requestobjects.password;

import lombok.Value;

@Value
public class ResetPassRequest {
    String email;
    int token;
    String password;
}
