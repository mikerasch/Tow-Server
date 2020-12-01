package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.queryinterfaces.EmailReminderInterface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * users repository to communicate with database
 */
@Repository
public interface UsersRepository extends CrudRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findById(UUID id);

    Optional<Users> findByResetToken(int resetToken);

    Boolean existsByUsername(String username);

    @Transactional
    @Modifying
    void deleteByEmail(String email);

    Boolean existsByEmail(String email);

    Users findByEmail(String emailString);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET ver_enabled = :isEnabled, verify_token = '' WHERE id = :id")
    void updateUserEmailVerifiedByUUID(@Param("id") UUID id, @Param("isEnabled") boolean verEnabled);

    @Query(value = "SELECT users.id, users.email, users.verify_token, users.verify_date, users.ver_enabled FROM users WHERE verify_token = :verify_token")
    Optional<Users> findByVerifyToken(@Param("verify_token") String verToken);

    @Query(value = "SELECT uuid, email, firstname, lastname, verify_token, verify_date FROM users WHERE ver_enabled = false")
    ArrayList<EmailReminderInterface> findAllNonVerified();
}
