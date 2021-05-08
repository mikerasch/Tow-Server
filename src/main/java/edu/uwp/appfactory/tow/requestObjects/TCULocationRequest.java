package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class TCULocationRequest {
    float latitude;
    float longitude;
    boolean active;
    String jwtToken;
}
