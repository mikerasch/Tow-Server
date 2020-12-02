package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class VerifyToken {
    @NotBlank
    @Getter
    @Setter
    private UUID id;

    @NotBlank
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Getter
    @Setter
    private String verify_token;

    @NotBlank
    @Getter
    @Setter
    private String verify_date;

    @NotBlank
    @Getter
    @Setter
    private boolean ver_enabled;
}
