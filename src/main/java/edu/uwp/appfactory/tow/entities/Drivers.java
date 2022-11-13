package edu.uwp.appfactory.tow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Drivers extends Users{

    public Drivers(String email, String username, String password,
                   String firstname, String lastname, String phone, String role){
        super(email,username,password,firstname,lastname,phone,role);
    }
}
