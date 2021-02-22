package edu.uwp.appfactory.tow.requestObjects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class PDUAuthRequest {
    @NotBlank
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Getter
    @Setter
    private String password;

    public PDUAuthRequest(String frontID, String password) {
        this.username = frontID;
        this.password = password;
    }
}
