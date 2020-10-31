/**
 * A dispatcher repo that may be needed in the future but currently does nothing.
 *
 */
package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DispatcherRepository extends JpaRepository<Driver, UUID> {



}
