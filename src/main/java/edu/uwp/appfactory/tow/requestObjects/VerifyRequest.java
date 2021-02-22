package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class VerifyRequest {
    @NotBlank
    @Getter
    @Setter
    private String token;

    public VerifyRequest(String verifyToken) {

        token = verifyToken;
    }
}