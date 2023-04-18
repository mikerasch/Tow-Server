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
@Table(name = "police_deparment_admins")
public class PDAdmin extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "address_number", nullable = false)
    private int addressNumber;
    @Column(name = "department", nullable = false)
    private String department;
    @Column(name = "department_short", nullable = false)
    private String departmentShort;
    public PDAdmin(String email, String username, String password,
                   String firstname, String lastname, String phone,
                   String role, String city, int addressNumber, String department, String departmentShort) {
        super(email, username, password, firstname, lastname, phone, role);
        this.city = city;
        this.addressNumber = addressNumber;
        this.department = department;
        this.departmentShort = departmentShort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PDAdmin pdAdmin = (PDAdmin) o;
        return id != null && Objects.equals(id, pdAdmin.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
