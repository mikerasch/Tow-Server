package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tow_company_admins")
public class TCAdmin extends Users{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "company", nullable = false)
    private String company;

    public TCAdmin(String email, String username, String password, String firstname, String lastname, String phone, String role, String company) {
        super(email,username,password,firstname,lastname,phone,role);
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TCAdmin tcAdmin = (TCAdmin) o;
        return id != null && Objects.equals(id, tcAdmin.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
