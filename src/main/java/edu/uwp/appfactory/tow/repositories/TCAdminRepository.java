package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface TCAdminRepository extends CrudRepository<TCAdmin, Long> {
    Optional<TCAdmin> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    @Transactional
    void deleteByUserEmail(String email);

}
