package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class SetLocationRequest {
    @NotBlank
    @Getter
    @Setter
    private float latitude;

    @NotBlank
    @Getter
    @Setter
    private float longitude;

    @NotBlank
    @Getter
    @Setter
    private String truck;

    @NotBlank
    @Getter
    @Setter
    private boolean active;
}
