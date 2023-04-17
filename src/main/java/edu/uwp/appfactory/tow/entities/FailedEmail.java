package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "failed_email")
public class FailedEmail {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "first_name", nullable = false)
    private String firstname;
    @Column(name = "last_name", nullable = false)
    private String lastname;
    @Column(name = "verify_token")
    private String verifyToken;
    @Column(name = "retries")
    private int retries;

    public FailedEmail(String email, Long id, String firstname, String lastname, String verifyToken) {
        this.email = email;
        this.userId = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.verifyToken = verifyToken;
        this.retries = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FailedEmail() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FailedEmail that = (FailedEmail) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
