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
@Table(name = "super_admin_admin")
public class SPAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Users user;
    public SPAdmin(String firstname, String lastname, String email, String password, String phone, String role, String username){
        user = new Users(email, username, password, firstname, lastname, phone, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SPAdmin spAdmin = (SPAdmin) o;
        return id != null && Objects.equals(id, spAdmin.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
