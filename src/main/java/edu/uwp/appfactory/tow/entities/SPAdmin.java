package edu.uwp.appfactory.tow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SPAdmin extends Users{
    public SPAdmin(String firstname, String lastname, String email, String password, String phone, String role, String username){
        super(email, username, password, firstname, lastname, phone, role);
    }
}
