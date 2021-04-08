package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.PDUser;
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
public interface PDUserRepository extends CrudRepository<PDUser, UUID> {
}




