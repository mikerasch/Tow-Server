package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class DriversRequest {
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
    private int radius;

    @NotBlank
    @Getter
    @Setter
    private String truck;
}
