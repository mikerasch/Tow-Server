package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SuperAdminRepository extends CrudRepository<SPAdmin, Long> {
    boolean existsByEmail(String email);
    Optional<SPAdmin> findByEmail(String email);
    @Transactional
    void deleteByEmail(String email);

}
