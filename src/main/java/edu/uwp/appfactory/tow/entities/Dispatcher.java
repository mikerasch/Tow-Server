package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dispatcher extends Users {
    private String precinct;

    public Dispatcher(String email, String username, String password, String firstname, String lastname, String phone, String role, String precinct) {
        super(email, username, password, firstname, lastname, phone, role);
        this.precinct = precinct;
    }
}
