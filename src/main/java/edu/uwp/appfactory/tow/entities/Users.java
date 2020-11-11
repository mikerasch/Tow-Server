package edu.uwp.appfactory.tow.entities;
import edu.uwp.appfactory.tow.data.IUser;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * The user entity interacts with the user table.
 */
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
    private String UUID; // look at java.uuid

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
    private int resetToken;
    private String resetDate;
    private String verifyDate;
    private boolean verEnabled;
    private String verifyToken;

    /**
     * constructor that takes 5 parameters
     */
    public Users(String email, String username, String password, String firstname, String lastname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /**
     * default constructor
     */
    public Users() { }

    /**
     * getters & setters
     */
    public String getUUID() {
        return UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRoles() {
        return role;
    }
    public void setRoles(String role) {
        this.role = role;
    }

    public int getResetToken() {
        return resetToken;
    }
    public void setResetToken(int resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetDate() {
        return resetDate;
    }
    public void setResetDate(String resetDate) {
        this.resetDate = resetDate;
    }

    public String getVerifyToken() {
        return verifyToken;
    }
    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getVerifyDate() {
        return verifyDate;
    }
    public void setVerifyDate(String verifyDate) { this.verifyDate = verifyDate; }

    public boolean getVerEnabled() {
        return verEnabled;
    }
    public void setVerEnabled(boolean verEnabled) { this.verEnabled = verEnabled; }
}
