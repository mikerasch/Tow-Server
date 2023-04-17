package edu.uwp.appfactory.tow.repositories;
import edu.uwp.appfactory.tow.entities.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    Optional<File> findByFilenameAndUserId(String filename, Long id);
    Optional<File> findByFilename(String filename);
}
