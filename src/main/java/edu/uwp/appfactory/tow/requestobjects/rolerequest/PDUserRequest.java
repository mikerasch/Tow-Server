package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.Value;

@Value
public class PDUserRequest {
    String firstname;
    String lastname;
    String email;
    String password;
}
