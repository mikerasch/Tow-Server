package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUsername(String username);

	Optional<Users> findByResetToken(String token);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query(value = "SELECT *, 0 as clazz_ FROM users WHERE email = ?1", nativeQuery = true)
	Users findByEmail(String emailString);

//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE users SET reset_token = ?1 where email = ?2 ", nativeQuery = true)
//	int updateResetToken(int resetToken, String email, String datetime);
}
