package edu.uwp.appfactory.tow.repositories;

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

///**
// * A dispatcher repo that may be needed in the future but currently does nothing.
// */
//@Repository
//public interface DispatcherRepository extends CrudRepository<Driver, String> {
//
//    @Query(value = "select * from driver as d where d.active = true AND (SELECT calculate_distance(:latitude, :longitude, d.latitude, d.longitude)) <= :radius")
//    List<Driver> findByDistance(@Param("latitude") float latitude, @Param("longitude") float longitude, @Param("radius") int radius);
//}




