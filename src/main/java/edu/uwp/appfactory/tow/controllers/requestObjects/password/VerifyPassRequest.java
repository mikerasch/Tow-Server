package edu.uwp.appfactory.tow.controllers.requestObjects.password;

import lombok.Value;

@Value
public class VerifyPassRequest {
    String email;
    int token;
}
