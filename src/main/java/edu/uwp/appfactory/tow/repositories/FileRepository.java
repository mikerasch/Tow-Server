package edu.uwp.appfactory.tow.repositories;
import edu.uwp.appfactory.tow.entities.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends CrudRepository<File, UUID> {
    Optional<File> findByFilename(String filename);
}
