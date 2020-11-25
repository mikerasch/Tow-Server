package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.queryinterfaces.PDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A dispatcher repo that may be needed in the future but currently does nothing.
 *
 */
@Repository
public interface DispatcherRepository extends JpaRepository<Driver, String> {

    /**
     * retrieves all drivers within a radius of the accident
     */
    @Query(value = "with use as (select * from driver where active = true) " +
            "SELECT t1.latitude, t1.longitude,  t2.* from use t1 inner join users t2 on t1.uuid = t2.uuid " +
            "where (SELECT calculate_distance(?1, ?2, t1.latitude, t1.longitude)) <= ?3", nativeQuery = true)
    List<PDriver> findByDistance(float latitude, float longitude, int radius);
}
