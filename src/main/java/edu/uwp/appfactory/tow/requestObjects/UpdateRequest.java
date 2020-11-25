package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class UpdateRequest {
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
