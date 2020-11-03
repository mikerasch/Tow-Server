/**
 * The driver entity interacts with the driver table.
 */
package edu.uwp.appfactory.tow.entities;

import edu.uwp.appfactory.tow.data.IDriver;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "Driver")
@Table(name = "driver")
@PrimaryKeyJoinColumn(name = "uuid")
public class Driver extends Users implements IDriver {

    /**
     * class level variables to hold the values until they are written to the db, or once they are pulled from the db.
     */
    private float latitude;
    private float longitude;
    private boolean active;

    /**
     * A constructor that takes 8 parameters
     * @param email the new drivers email  
     * @param username the new drivers username
     * @param password the new drivers password
     * @param firstname the new drivers first name
     * @param lastname the new drivers last name
     * @param latitude the drivers initial UTM lat ( may be null during creation)
     * @param longitude the drivers initial UTM long ( may be null during creation)
     * @param active the drivers availability status, will likely start at false
     */
    public Driver(String email, String username, String password, String firstname, String lastname, float latitude, float longitude, boolean active) {
        super(email, username, password, firstname, lastname);
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
    }

    /**
     * default constructor
     */
    public Driver() {

    }

    /**
     * Getters & Setters
     */

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
