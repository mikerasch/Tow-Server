package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCUser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface TCUserRepository extends CrudRepository<TCUser, UUID> {
    @Query(value = "select * from tc_user where admin_uuid = :uuid")
    List<TCUser> findAllByAdminUUID(@Param("uuid") UUID adminUUID);
}




