package edu.uwp.appfactory.tow.controllers.requestObjects.location;

import lombok.Value;

@Value
public class TCULocationRequest {
    float latitude;
    float longitude;
    boolean active;
    String jwtToken;
}
