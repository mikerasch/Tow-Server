package edu.uwp.appfactory.tow.requestobjects.password;

import lombok.Value;

@Value
public class VerifyPassRequest {
    String email;
    int token;
}
