package edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest;

import lombok.Value;

@Value
public class PDAdminRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
    String city;
    int addressNumber;
    String department;
    String departmentShort;
}
