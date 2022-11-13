package edu.uwp.appfactory.tow.requestObjects.rolerequest;

import lombok.Value;

@Value
public class DriverRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
}
