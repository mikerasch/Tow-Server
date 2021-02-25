package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class VerifyPassRequest {
    String email;
    int token;
}
