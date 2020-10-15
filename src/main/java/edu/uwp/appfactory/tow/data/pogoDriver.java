package edu.uwp.appfactory.tow.data;

public interface pogoDriver {
    String getUser_UUID();
    float getLatitude();
    float getLongitude();
    boolean isActive();
}
