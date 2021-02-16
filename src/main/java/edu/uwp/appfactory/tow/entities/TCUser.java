package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TCUser extends Users {

    private String company;

    public TCUser(String email, String username, String password, String firstname, String lastname, String phone, String role, String company) {
        super(email, username, password, firstname, lastname, phone, role);
        this.company = company;
    }
}
