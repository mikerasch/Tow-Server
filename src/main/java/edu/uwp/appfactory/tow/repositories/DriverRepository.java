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

	@Query(value = "with use as (select * from driver where active = true) SELECT t1.latitude, t1.longitude, t2.* from use t1 inner join users t2 on t1.uuid = t2.uuid " +
			"where (SELECT calculate_distance(?1, ?2, t1.latitude, t1.longitude)) <= ?3", nativeQuery = true)
	List<PDriver> findAllByDistance(float latitude, float longitude, int radius);



}//(select * from users where uuid = use.uuid ) as use2where (SELECT calculate_distance(?1, ?2, latitude, longitude))
//(select * from users where uuid = use.uuid )
//insert into public.users (email, firstname, lastname, password, reset_token, role, username, uuid) values (?, ?, ?, ?, ?, ?, ?, ?);
//
//insert into driver (active, latitude, longitude, user_uuid) values (?, ?, ?, ?)
//
//update driver set latitude = 42.571570, longitude = -87.892586, active = true where user_uuid = '73a4f04a-d1bb-4c8e-aa1c-f46d41ab0d1b';