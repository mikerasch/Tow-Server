package edu.uwp.appfactory.tow.responseobjects;

import lombok.Value;

@Value
public class TCUserResponse {
    String username;
    String email;
    String firstname;
    String lastname;
    String phone;
    String company;
}
