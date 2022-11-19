package edu.uwp.appfactory.tow.webSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.entities.UsersDTO;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * users repository to communicate with database
 */
@Repository
public interface UsersRepository extends CrudRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String emailString);

    Optional<Users> findById(UUID id);

    @Query("SELECT id from users where email = :email")
    UUID findIDByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE users SET email = :email, firstname = :firstname, lastname = :lastname, role = :role, username = :username, " +
            "ver_enabled = :verEnabled WHERE id = :id")
    void updateUser(@Param("id") UUID id, @Param("email")String email, @Param("firstname")String firstname,
                       @Param("lastname")String lastname,@Param("role")String role,
                       @Param("username")String username,@Param("verEnabled") boolean verEnabled);


    @Query("SELECT * FROM users")
    List<Users> getAllUsers();

    Optional<Users> findByResetToken(int resetToken);

    Boolean existsByUsername(String username);

    @Transactional
    @Modifying
    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET ver_enabled = :isEnabled, verify_token = '' WHERE id = :id")
    void updateUserEmailVerifiedByUUID(@Param("id") UUID id, @Param("isEnabled") boolean verEnabled);

    @Query(value = "SELECT users.id, users.email, users.verify_token, users.verify_date, users.ver_enabled FROM users WHERE verify_token = :verify_token")
    Optional<Users> findByVerifyToken(@Param("verify_token") String verToken);

    @Query(value = "SELECT * FROM users WHERE ver_enabled = false")
    List<Users> findAllNonVerified();
}
