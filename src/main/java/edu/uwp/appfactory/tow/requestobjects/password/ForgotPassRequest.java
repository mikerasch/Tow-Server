package edu.uwp.appfactory.tow.requestobjects.password;

import lombok.Value;

@Value
public class ForgotPassRequest {
    String email;
    String platform;
}
