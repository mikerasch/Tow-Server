package edu.uwp.appfactory.tow.WebSecurityConfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.Role;

import java.util.Optional;

/**
 * role repository to communicate with database
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    //find the role with the offered name of role
    Optional<Role> findByName(ERole name);
}