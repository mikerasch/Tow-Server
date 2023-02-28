package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Requests;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends CrudRepository<Requests, UUID> {
    @Query("SELECT * FROM requests WHERE driver_id = :driverId")
    List<Requests> findAllByDriverId(UUID driverId);
}
