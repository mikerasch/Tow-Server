package edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest;

import lombok.Value;

import java.util.UUID;

@Value
public class VerifyToken {
    UUID id;
    String email;
    String verify_token;
    String verify_date;
    boolean ver_enabled;
}
