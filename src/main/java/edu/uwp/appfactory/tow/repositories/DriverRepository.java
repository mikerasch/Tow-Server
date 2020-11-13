package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {

	/**
	 * finds user by UUID
	 */
	@Query(value = "SELECT t1.*, t2.* from driver t1 inner join users t2 on t1.uuid = ?1 AND t2.uuid = ?1", nativeQuery = true)
	Optional<Driver> findByUUID(String UUID);
}




