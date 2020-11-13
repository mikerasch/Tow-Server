package edu.uwp.appfactory.tow.entities;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 *
 */
@Entity
@Table(name = "failed_email")
public class FailedEmail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )

    /**
     *
     */
    private UUID uuid;
    private String email;
    private String firstname;
    private String lastname;
    private String user_uuid;
    private String verify_token;
    private int retries;

    /**
     *
     */
    public FailedEmail(String email, String user_uuid, String firstname, String lastname, String verify_token) {
        this.email = email;
        this.user_uuid = user_uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.verify_token = verify_token;
    }

    /**
     *
     */
    public FailedEmail() { }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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

    public String getUser_uuid() {
        return user_uuid;
    }
    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getVerify_token() {
        return verify_token;
    }
    public void setVerify_token(String verify_token) {
        this.verify_token = verify_token;
    }

    public int getRetries() {
        return retries;
    }
    public void setRetries(int retries) {
        this.retries = retries;
    }
}
