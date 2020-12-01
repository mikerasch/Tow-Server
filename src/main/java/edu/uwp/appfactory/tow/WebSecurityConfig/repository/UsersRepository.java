package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.queryinterfaces.EmailReminderInterface;
import edu.uwp.appfactory.tow.queryinterfaces.VerifyTokenInterface;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Query(value = "UPDATE users SET  ver_enabled = ?2, verify_token = '' WHERE uuid = ?1")
    void updateUserEmailVerifiedByUUID(String uuid, boolean verEnabled);

    @Query(value = "SELECT uuid, email, verify_token, verify_date, ver_enabled FROM users WHERE verify_token = ?1")
    Optional<VerifyTokenInterface> findByVerifyToken(String verToken);

    @Query(value = "SELECT uuid, email, firstname, lastname, verify_token, verify_date FROM users WHERE ver_enabled = false")
    ArrayList<EmailReminderInterface> findAllNonVerified();
}
