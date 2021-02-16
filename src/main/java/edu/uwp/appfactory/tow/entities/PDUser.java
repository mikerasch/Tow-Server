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
public class PDUser extends Users {

    private String frontID;
    private UUID adminUUID;

    public PDUser(String email, String username, String password, String firstname, String lastname, String phone, String role, String frontID, UUID adminUUID) {
        super(email, username, password, firstname, lastname, phone, role);
        this.frontID = frontID;
        this.adminUUID = adminUUID;
    }
}
