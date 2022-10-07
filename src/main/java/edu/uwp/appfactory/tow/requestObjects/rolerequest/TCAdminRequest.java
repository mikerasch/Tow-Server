package edu.uwp.appfactory.tow.requestObjects.rolerequest;

import lombok.Value;

// todo: "import javax.validation.constraints.NotBlank;" NotBlank isn't working

@Value
public class TCAdminRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
    String company;
}
