package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class PDAdminRequest {
    @NotBlank
    @Getter
    @Setter
    private String firstname;

    @NotBlank
    @Getter
    @Setter
    private String lastname;

    @NotBlank
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Getter
    @Setter
    private String password;

    @NotBlank
    @Getter
    @Setter
    private String phone;

    @NotBlank
    @Getter
    @Setter
    private String city;

    @NotBlank
    @Getter
    @Setter
    private int addressNumber;

    @NotBlank
    @Getter
    @Setter
    private String department;

    @NotBlank
    @Getter
    @Setter
    private String departmentShort;
}
