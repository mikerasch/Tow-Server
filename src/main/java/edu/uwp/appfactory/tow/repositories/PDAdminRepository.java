package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.entities.PDAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface PDAdminRepository extends CrudRepository<PDAdmin, UUID> {

}




