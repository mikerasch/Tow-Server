package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 100)
    @Column(name = "username", nullable = false)
    private String username;

    @Size(max = 100)
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 120)
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 20)
    @Column(name = "first_name", nullable = false)
    private String firstname;

    @Size(max = 20)
    @Column(name = "last_name", nullable = false)
    private String lastname;
    @Column(name = "role", nullable = false)
    private String role;
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;
    @Column(name = "reset_token")
    private Integer resetToken;
    @Column(name = "reset_date")
    private String resetDate;
    @Column(name = "verify_date")
    private String verifyDate;
    @Column(name = "ver_enabled")
    private Boolean verEnabled;
    @Column(name = "verify_token")
    private String verifyToken;
    @Column(name = "fb_token")
    private String fbToken;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    public Users(String email, String username, String password, String firstname, String lastname, String phone, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Users users = (Users) o;
        return id != null && Objects.equals(id, users.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addFiles(File file) {
        file.setUser(this);
        this.files.add(file);
    }
}
