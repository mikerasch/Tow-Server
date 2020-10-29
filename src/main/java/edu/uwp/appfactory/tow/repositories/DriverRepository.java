package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.data.PDriver;
import edu.uwp.appfactory.tow.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {

    @Query(value = "with use as (select * from driver where active = true) " +
            "SELECT t1.latitude, t1.longitude, t2.* from use t1 inner join users t2 on t1.uuid = t2.uuid " +
            "where (SELECT calculate_distance(?1, ?2, t1.latitude, t1.longitude)) <= ?3", nativeQuery = true)
    List<PDriver> findAllByDistance(float latitude, float longitude, int radius);



	static Driver findByEmailIdIgnoreCase(String emailId) {
		return null;
	}

//	@Query(value = "UPDATE driver" +
//	"SET active = true, t1.latitude = latitude, t1.longitude = longitude" +
//	"WHERE uuid = this.uuid")
//	void getLocation(float latitude, float longitude, String uuid, boolean active);

}




