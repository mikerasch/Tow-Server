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
@Table(name = "drivers")
public class Drivers extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "latitude")
    private Float latitude;
    @Column(name = "longitude")
    private Float longitude;

    public Drivers(String email, String username, String password,
                   String firstname, String lastname, String phone, String role){
        super(email, username, password, firstname, lastname, phone, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Drivers drivers = (Drivers) o;
        return getId() != null && Objects.equals(getId(), drivers.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
