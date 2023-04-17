package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.PDUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface PDUserRepository extends CrudRepository<PDUser, Long> {
    Optional<PDUser> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    @Transactional
    void deleteByUserEmail(String email);

}




