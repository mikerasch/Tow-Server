package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Requests;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends CrudRepository<Requests, Long> {
    List<Requests> findAllByDriverId(Long driverId);
}
