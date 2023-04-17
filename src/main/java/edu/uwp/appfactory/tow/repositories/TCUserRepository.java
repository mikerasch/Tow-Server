package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * a repository that uses the find by distance  sql function to retreive every tow truck within a certain radius
 * of a point that the pd user sets.
 */
@Repository
public interface TCUserRepository extends CrudRepository<TCUser, Long> {
    List<TCUser> findAllByTcAdminId(Long adminUUID);

    Optional<TCUser> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    @Transactional
    void deleteByUserEmail(String email);
}




