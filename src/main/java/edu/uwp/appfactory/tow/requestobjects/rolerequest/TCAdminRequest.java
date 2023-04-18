package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.Value;

@Value
public class TCAdminRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
    String company;
}
