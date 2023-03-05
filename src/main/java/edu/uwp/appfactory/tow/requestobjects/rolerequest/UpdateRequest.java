package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.Value;

@Value
public class UpdateRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
}
