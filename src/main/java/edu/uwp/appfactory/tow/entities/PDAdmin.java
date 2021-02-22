package edu.uwp.appfactory.tow.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PDAdmin extends Users {

    private String city;
    private int addressNumber;
    private String department;
    private String departmentShort;

    public PDAdmin(String email, String username, String password,
                   String firstname, String lastname, String phone,
                   String role, String city, int addressNumber, String department, String departmentShort) {

        super(email, username, password, firstname, lastname, phone, role);
        this.city = city;
        this.addressNumber = addressNumber;
        this.department = department;
        this.departmentShort = departmentShort;
    }
}
