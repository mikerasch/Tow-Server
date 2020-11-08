/**
 * The user entity interacts with the user table.
 */
package edu.uwp.appfactory.tow.entities;
import edu.uwp.appfactory.tow.data.IUser;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;


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

    /**
     * A collection of attributes that will be the constraints of the users table
     */
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
     * default constructor
     */
    public Users() {
    }

    /**
     * constructor that takes 5 parameters
     * @param email
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     */
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
    public int getResetToken() {
        return resetToken;
    }

    @Override
    public void setResetToken(int resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public String getResetDate() {
        return resetDate;
    }

    @Override
    public void setResetDate(String resetDate) {
        this.resetDate = resetDate;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    @Override
    public String getVerifyDate() {
        return verifyDate;
    }

    @Override
    public void setVerifyDate(String verifyDate) {
        this.verifyDate = verifyDate;

    }

    @Override
    public boolean getVerEnabled() {
        return verEnabled;
    }

    @Override
    public void setVerEnabled(boolean verEnabled) {

        this.verEnabled = verEnabled;
    }

}
