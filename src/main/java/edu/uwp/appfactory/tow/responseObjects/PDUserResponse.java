package edu.uwp.appfactory.tow.responseObjects;

import lombok.Value;

@Value
public class PDUserResponse {
    String username;
    String email;
    String firstname;
    String lastname;
    String phone;
    String company;
}
