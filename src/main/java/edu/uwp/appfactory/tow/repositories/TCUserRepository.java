package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCUser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * a repository that uses the find by distance  sql function to retreive every tow truck within a certain radius
 * of a point that the pd user sets.
 */
@Repository
public interface TCUserRepository extends CrudRepository<TCUser, UUID> {
    @Query(value = "select * from tc_user as d where d.active = true AND (SELECT calculate_distance(:latitude, :longitude, d.latitude, d.longitude)) <= :radius")
    List<TCUser> findByDistance(@Param("latitude") float latitude, @Param("longitude") float longitude, @Param("radius") int radius);


    @Query(value = "select * from tc_user where admin_uuid = :uuid")
    List<TCUser> findAllByAdminUUID(@Param("uuid") UUID adminUUID);

    Optional<TCUser> findByEmail(String email);

}




