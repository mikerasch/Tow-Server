package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface SuperAdminRepository extends CrudRepository<SPAdmin, UUID> {
    boolean existsByEmail(String email);
    Optional<SPAdmin> findByEmail(String email);
}
