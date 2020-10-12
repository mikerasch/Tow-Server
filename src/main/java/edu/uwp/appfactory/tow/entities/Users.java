package edu.uwp.appfactory.tow.entities;

import edu.uwp.appfactory.tow.data.IUser;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users", schema = "public",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email" , "username"})
})
@Inheritance(strategy = InheritanceType.JOINED)
public class Users implements IUser {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String UUID;

    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String firstname;

    @NotBlank
    @Size(max = 20)
    private String lastname;

    @NotBlank
    private String role;

    private String resetToken;

    public Users() {
    }

    public Users(String email, String username, String password, String firstname, String lastname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /**
     * getters & setters
     */

    @Override
    public String getUUID() {
        return UUID;
    }

    @Override
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String getRoles() {
        return role;
    }

    @Override
    public void setRoles(String role) {
        this.role = role;
    }

    @Override
    public String getResetToken() {
        return resetToken;
    }

    @Override
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

}
