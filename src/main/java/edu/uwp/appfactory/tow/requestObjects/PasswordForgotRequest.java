package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class PasswordForgotRequest {
    @NotBlank
    @Getter
    @Setter
    private String email;
}
