package edu.uwp.appfactory.tow.requestobjects.location;

import lombok.Value;

@Value
public class DriversRadiusRequest {
    float latitude;
    float longitude;
    int radius;
}
