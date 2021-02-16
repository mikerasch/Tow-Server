package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.PDAdmin;
import edu.uwp.appfactory.tow.entities.TCAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface TCAdminRepository extends CrudRepository<TCAdmin, UUID> {

}




