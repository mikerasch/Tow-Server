package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    /**
     *
     * @param username
     * @return
     */
    Optional<Users> findByUsername(String username);

    /**
     *
     * @param resetToken
     * @return
     */
    Optional<Users> findByResetToken(int resetToken);

    /**
     *
     * @param username
     * @return
     */
    Boolean existsByUsername(String username);

    /**
     *
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);

    /**
     *
     * @param emailString
     * @return
     */
    @Query(value = "SELECT *, 0 as clazz_ FROM users WHERE email = ?1", nativeQuery = true)
    Users findByEmail(String emailString);

//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE users SET reset_token = ?1 where email = ?2 ", nativeQuery = true)
//	int updateResetToken(int resetToken, String email, String datetime);
}
