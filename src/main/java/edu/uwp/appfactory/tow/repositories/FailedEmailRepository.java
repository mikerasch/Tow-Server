package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.FailedEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FailedEmailRepository extends CrudRepository<FailedEmail, UUID> { // t=object id=type of id

}
