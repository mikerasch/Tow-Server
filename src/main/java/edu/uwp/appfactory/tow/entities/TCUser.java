package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "tow_company_users")
public class TCUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "latitude")
    private Float latitude;
    @Column(name = "longitude")
    private Float longitude;
    @Column(name = "active")
    private Boolean active;
    @OneToOne(cascade = CascadeType.ALL)
    private TCAdmin tcAdmin;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Users user;

    public TCUser(String email, String username, String password,
                  String firstname, String lastname, String phone,
                  String role, Float latitude,
                  Float longitude, Boolean active, TCAdmin tcAdmin) {
        this.user = new Users(email,username,password,firstname,lastname,phone,role);
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
        this.tcAdmin = tcAdmin;
    }
    public TCUser(String email, String username, String password,
                  String firstname, String lastname, String phone,
                  String role, Float latitude,
                  Float longitude, Boolean active) {
        this.user = new Users(email,username,password,firstname,lastname,phone,role);
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TCUser tcUser = (TCUser) o;
        return id != null && Objects.equals(id, tcUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
