package edu.uwp.appfactory.tow.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class UsersDTO {
    private UUID id;
    private String email;
    private String firstname;
    private String lastname;
    private String role;
    private String username;
    private boolean verEnabled;
    public UsersDTO(UUID id, String email, String firstname, String lastname, String role, String username, boolean verEnabled){
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.username = username;
        this.verEnabled = verEnabled;
    }

    public boolean getVerEnabled() {
        return verEnabled;
    }
}
