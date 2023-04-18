package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface TCAdminRepository extends CrudRepository<TCAdmin, Long> {
    Optional<TCAdmin> findByEmail(String email);
    boolean existsByEmail(String email);
    @Transactional
    void deleteByEmail(String email);
}
