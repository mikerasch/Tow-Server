package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SuperAdminRepository extends CrudRepository<SPAdmin, Long> {
    boolean existsByUserEmail(String email);
    Optional<SPAdmin> findByUserEmail(String email);
    @Transactional
    void deleteByUserEmail(String email);

}
