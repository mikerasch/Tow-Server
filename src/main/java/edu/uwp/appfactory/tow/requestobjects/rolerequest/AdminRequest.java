package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.Value;

@Value
public class AdminRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
}
