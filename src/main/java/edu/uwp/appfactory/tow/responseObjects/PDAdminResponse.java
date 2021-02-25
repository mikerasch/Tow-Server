package edu.uwp.appfactory.tow.responseObjects;

import lombok.Value;

@Value
public class PDAdminResponse {
    String username;
    String email;
    String firstname;
    String lastname;
    String phone;
    String company;
}
