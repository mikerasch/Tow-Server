package edu.uwp.appfactory.tow.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("sp_admin")
@NoArgsConstructor
@EqualsAndHashCode
public class SuperAdmin {
    @Id
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String username;

    private String verifyToken;
    public SuperAdmin(String firstname, String lastname, String email, String password, String phone, String role, String username){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.username = username;
    }
}
