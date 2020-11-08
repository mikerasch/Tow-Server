/**
 * a repository that will communicate between the entities and the databases
 */

package edu.uwp.appfactory.tow.repositories;
import edu.uwp.appfactory.tow.queryinterfaces.PDriver;
import edu.uwp.appfactory.tow.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {

	/**
	 * retrieves all drivers within a radius of the accident
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @return
	 */
    @Query(value = "with use as (select * from driver where active = true) " +
            "SELECT t1.latitude, t1.longitude, t2.* from use t1 inner join users t2 on t1.uuid = t2.uuid " +
            "where (SELECT calculate_distance(?1, ?2, t1.latitude, t1.longitude)) <= ?3", nativeQuery = true)
    List<PDriver> findAllByDistance(float latitude, float longitude, int radius);

	/**
	 * finds user by UUID
	 * @param UUID of user
	 * @return user object of
	 */
	@Query(value = "SELECT t1.*, t2.* from driver t1 inner join users t2 on t1.uuid = ?1 AND t2.uuid = ?1", nativeQuery = true)
	Optional<Driver> findByUUID(String UUID);


}




