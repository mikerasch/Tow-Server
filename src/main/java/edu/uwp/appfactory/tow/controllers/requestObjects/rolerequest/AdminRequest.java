package edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest;

import lombok.Value;

@Value
public class AdminRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
}
