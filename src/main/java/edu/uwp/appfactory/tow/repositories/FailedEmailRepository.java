package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.FailedEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FailedEmailRepository extends JpaRepository<FailedEmail, UUID> { // t=object id=type of id

}
