package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.queryinterfaces.PDriver;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A dispatcher repo that may be needed in the future but currently does nothing.
 *
 */
@Repository
public interface DispatcherRepository extends CrudRepository<Driver, String> {

    @Query(value = "select * from driver as d where d.active = true AND (SELECT calculate_distance(:latitude, :longitude, d.latitude, d.longitude)) <= :radius")
    List<Driver> findByDistance(@Param("latitude") float latitude,@Param("longitude") float longitude,@Param("radius") int radius);
}
