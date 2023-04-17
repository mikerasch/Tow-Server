package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.FailedEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedEmailRepository extends CrudRepository<FailedEmail, Long> { // t=object id=type of id

}
