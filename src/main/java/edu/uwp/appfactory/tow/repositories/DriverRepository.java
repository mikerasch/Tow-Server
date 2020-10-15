package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.data.pogoDriver;
import edu.uwp.appfactory.tow.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {

	@Query(value = "select * from driver where (SELECT calculate_distance(?1, ?2, latitude, longitude)) <= ?3 and active = true", nativeQuery = true)
	List<pogoDriver> findAllByDistance(float latitude, float longitude, int radius);

}

//insert into public.users (email, firstname, lastname, password, reset_token, role, username, uuid) values (?, ?, ?, ?, ?, ?, ?, ?);
//
//insert into driver (active, latitude, longitude, user_uuid) values (?, ?, ?, ?)
//
//update driver set latitude = 42.571570, longitude = -87.892586, active = true where user_uuid = '73a4f04a-d1bb-4c8e-aa1c-f46d41ab0d1b';