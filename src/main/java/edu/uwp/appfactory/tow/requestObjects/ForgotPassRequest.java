package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class ForgotPassRequest {
    String email;
    String platform;
}
