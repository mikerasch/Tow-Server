package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Driver;
import java.util.List;
import java.util.UUID;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface TCAdminRepository extends CrudRepository<TCAdmin, UUID> {


}

///**
// * A dispatcher repo that may be needed in the future but currently does nothing.
// */
//@Repository
//interface DispatcherRepository extends CrudRepository<Driver, String> {
//
//}




