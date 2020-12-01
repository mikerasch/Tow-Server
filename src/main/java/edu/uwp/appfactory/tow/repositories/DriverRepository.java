package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface DriverRepository extends CrudRepository<Driver, UUID> {

	@Query(value = "SELECT t1.*, t2.* from driver t1 inner join users t2 on t1.uuid = ?1 AND t2.uuid = ?1")
	Optional<Driver> findByUUID(String UUID);

}




