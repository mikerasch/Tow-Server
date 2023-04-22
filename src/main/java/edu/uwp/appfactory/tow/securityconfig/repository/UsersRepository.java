package edu.uwp.appfactory.tow.securityconfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * users repository to communicate with database
 */
@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String emailString);

    Optional<Users> findByPhone(String phone);
    @Query("SELECT id from Users where email = :email")
    Long findIDByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Users SET email = :email, firstname = :firstname, lastname = :lastname, role = :role, username = :username, " +
            "verEnabled = :verEnabled WHERE id = :id")
    void updateUser(@Param("id") Long id, @Param("email")String email, @Param("firstname")String firstname,
                       @Param("lastname")String lastname,@Param("role")String role,
                       @Param("username")String username,@Param("verEnabled") boolean verEnabled);


    @Query("SELECT u FROM Users u")
    List<Users> getAllUsers();

    Optional<Users> findByResetToken(int resetToken);

    Boolean existsByUsername(String username);

    @Transactional
    @Modifying
    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users SET verEnabled = :isEnabled, verifyToken = '' WHERE id = :id")
    void updateUserEmailVerifiedByUUID(@Param("id") Long id, @Param("isEnabled") boolean verEnabled);
    Optional<Users> findByVerifyToken(String verToken);

    @Query("SELECT u FROM Users u WHERE u.verEnabled = false")
    List<Users> findAllNonVerified();
}
