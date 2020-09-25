package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Users, Long> {
    // query DB to select any user from users where email == findByEmail:email param, and that user role == dispatcher
    @Query(value = "SELECT * FROM Users WHERE email=?1 AND role='ROLE_ADMIN'", nativeQuery = true)
    Optional<Users> findByEmail(String email); // optional because user may !exist

    @Query(value = "SELECT * FROM Users WHERE id=?1 AND role='ROLE_ADMIN'", nativeQuery = true)
    Optional<Users> findById(long id);

    @Query(value = "SELECT * FROM Users WHERE role='ROLE_ADMIN'", nativeQuery = true)
    List<Users> findAllAdmin();
}
