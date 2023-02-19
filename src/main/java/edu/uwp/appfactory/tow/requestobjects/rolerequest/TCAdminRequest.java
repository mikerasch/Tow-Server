package edu.uwp.appfactory.tow.requestobjects.rolerequest;

import lombok.Value;

// todo: "import jakarta.validation.constraints.NotBlank;" NotBlank isn't working

@Value
public class TCAdminRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
    String company;
}
