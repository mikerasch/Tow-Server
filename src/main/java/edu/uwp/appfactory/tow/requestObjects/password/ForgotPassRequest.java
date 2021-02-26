package edu.uwp.appfactory.tow.requestObjects.password;

import lombok.Value;

@Value
public class ForgotPassRequest {
    String email;
    String platform;
}
