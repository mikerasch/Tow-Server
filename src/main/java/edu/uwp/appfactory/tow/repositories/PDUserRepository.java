package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.PDUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * a repository that will communicate between the entities and the databases
 */
@Repository
public interface PDUserRepository extends CrudRepository<PDUser, UUID> {
//    @Query(value = "select * from driver as d where d.active = true AND (SELECT calculate_distance(:latitude, :longitude, d.latitude, d.longitude)) <= :radius")
//    List<PDUser> findByDistance(@Param("latitude") float latitude, @Param("longitude") float longitude, @Param("radius") int radius);
}




