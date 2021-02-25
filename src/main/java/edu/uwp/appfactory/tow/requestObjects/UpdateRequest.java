package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class UpdateRequest {
    String firstname;
    String lastname;
    String email;
    String phone;
}
