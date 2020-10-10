package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import edu.uwp.appfactory.tow.testingEntities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUsername(String username);

	Optional<Users> findByResetToken(String token);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query(value = "SELECT * FROM users WHERE email like ?1", nativeQuery = true)
	Users findByEmail(String emailString);
}
