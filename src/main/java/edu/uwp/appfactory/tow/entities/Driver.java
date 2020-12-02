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
    private String truck_type;

    public Driver(String email, String username, String password, String firstname, String lastname, String phone, float latitude, float longitude, String truck_type, boolean active) {
        super(email, username, password, firstname, lastname, phone);
        this.latitude = latitude;
        this.longitude = longitude;
        this.truck_type = truck_type;
        this.active = active;
    }
}
