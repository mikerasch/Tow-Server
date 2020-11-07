package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.queryinterfaces.VerifyTokenInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * users repository to communicate with database
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByResetToken(int resetToken);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Users findByEmail(String emailString);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET  ver_enabled = ?2, verify_token = '' WHERE uuid = ?1", nativeQuery = true)
    void updateUserEmailVerifiedByUUID(String uuid, boolean verEnabled);

    @Query(value = "SELECT uuid, email, verify_token, verify_date, ver_enabled FROM users WHERE verify_token = ?1", nativeQuery = true)
    Optional<VerifyTokenInterface> findByVerifyToken(String verToken);

    @Query(value = "SELECT *, 0 as clazz_ FROM users WHERE uuid = ?1", nativeQuery = true)
    Optional<Users> findByUUID(String UUID);
}
