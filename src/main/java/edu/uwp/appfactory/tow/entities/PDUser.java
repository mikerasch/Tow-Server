package edu.uwp.appfactory.tow.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "police_department_users")
public class PDUser extends Users {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "front_id")
    private String frontID;
    @Column(name = "admin_id")
    private Long adminId;

    public PDUser(String email, String username, String password, String firstname, String lastname, String phone, String role, String frontID, long adminId) {
        super(email, username, password, firstname, lastname, phone, role);
        this.frontID = frontID;
        this.adminId = adminId;
    }
}
