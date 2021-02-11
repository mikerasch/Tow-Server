package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class FileRequest {
    @NotBlank
    @Getter
    @Setter
    private byte[] data;

}