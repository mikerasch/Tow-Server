package edu.uwp.appfactory.tow.queryinterfaces;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UpdateUser {
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
    private String phone;
}
