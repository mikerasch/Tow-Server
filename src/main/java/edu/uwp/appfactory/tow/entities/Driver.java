package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Driver extends Users {

    private Float latitude;
    private Float longitude;
    private Boolean active;

    public Driver(String email, String username, String password, String firstname, String lastname, String phone, String verifyToken, float latitude, float longitude, boolean active) {
        super(email, username, password, firstname, lastname, phone, verifyToken);
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
    }
}
