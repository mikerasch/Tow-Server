package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * users repository to communicate with database
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    /**
     * finds user by username
     * @param username of user
     * @return user object of
     */
    Optional<Users> findByUsername(String username);

    /**
     * find by reset token
     * @param resetToken of a user
     * @return user
     */
    Optional<Users> findByResetToken(int resetToken);

    /**
     * does this user exist with the given username
     * @param username of a user
     * @return boolean if the user exists
     */
    Boolean existsByUsername(String username);

    /**
     * does this user exist by the email provided
     * @param email of a user
     * @return boolean if the user exists
     */
    Boolean existsByEmail(String email);


    /**
     * find a user where the email matches anything in the database
     * @param emailString of a user
     * @return user
     */
    @Query(value = "SELECT *, 0 as clazz_ FROM users WHERE email = ?1", nativeQuery = true)
    Users findByEmail(String emailString);


    /**
     * find a user where the email matches anything in the database
     * @param verToken of a user
     * @return user
     */
    @Query(value = "SELECT *, 0 as clazz_ FROM users WHERE verify_token = ?1", nativeQuery = true)
    Optional<Users> findByVerToken(String verToken);


    @Query(value = "SELECT *, 0 as clazz_ FROM users WHERE uuid = ?1", nativeQuery = true)
    Optional<Users> findByUUID(String UUID);



//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE users SET reset_token = ?1 where email = ?2 ", nativeQuery = true)
//	int updateResetToken(int resetToken, String email, String datetime);
}
