package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.SuperAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface SuperAdminRepository extends CrudRepository<SuperAdmin, UUID> {
    boolean existsByEmail(String email);
    Optional<SuperAdmin> findByEmail(String email);
}
