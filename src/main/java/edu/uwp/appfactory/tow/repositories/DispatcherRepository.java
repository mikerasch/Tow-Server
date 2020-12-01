package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.queryinterfaces.PDriver;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A dispatcher repo that may be needed in the future but currently does nothing.
 *
 */
@Repository
public interface DispatcherRepository extends CrudRepository<Driver, String> {

    @Query(value = "with use as (select * from driver where active = true) " +
            "SELECT t1.latitude, t1.longitude,  t2.* from use t1 inner join users t2 on t1.uuid = t2.uuid " +
            "where (SELECT calculate_distance(?1, ?2, t1.latitude, t1.longitude)) <= ?3")
    List<Driver> findByDistance(float latitude, float longitude, int radius);
}
