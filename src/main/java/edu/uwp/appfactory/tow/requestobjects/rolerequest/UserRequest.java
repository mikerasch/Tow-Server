package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String fbToken;
}
