package edu.uwp.appfactory.tow.requestobjects.users;

import lombok.Data;

@Data
public class UsersDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String role;
    private String username;
    private boolean verEnabled;
    public UsersDTO(Long id, String email, String firstname, String lastname, String role, String username, boolean verEnabled){
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
