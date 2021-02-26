package edu.uwp.appfactory.tow.requestObjects.password;

import lombok.Value;

@Value
public class VerifyPassRequest {
    String email;
    int token;
}
