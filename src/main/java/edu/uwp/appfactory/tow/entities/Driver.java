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
     * @param email
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @param latitude
     * @param longitude
     * @param active
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

    public void setEnabled(boolean b) {
    }
}
