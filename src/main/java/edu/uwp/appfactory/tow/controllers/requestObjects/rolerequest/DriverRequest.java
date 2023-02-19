package edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
}
