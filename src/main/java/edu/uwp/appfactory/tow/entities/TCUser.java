package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TCUser extends Users {

    private Float latitude;
    private Float longitude;
    private Boolean active;
    private UUID adminUUID;

    public TCUser(String email, String username, String password,
                  String firstname, String lastname, String phone,
                  String role, Float latitude,
                  Float longitude, Boolean active, UUID adminUUID) {

        super(email, username, password, firstname, lastname, phone, role);
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.adminUUID = adminUUID;
    }
}
