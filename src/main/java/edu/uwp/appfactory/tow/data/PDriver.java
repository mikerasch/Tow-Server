/**
 * Plain old java object for the driver
 */
package edu.uwp.appfactory.tow.data;

public interface PDriver {
    String getFirstname();
    String getLastname();
    String getEmail();
    String getUUID();
    float getLatitude();
    float getLongitude();
}
