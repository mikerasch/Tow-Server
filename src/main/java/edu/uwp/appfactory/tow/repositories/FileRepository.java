package edu.uwp.appfactory.tow.repositories;
import edu.uwp.appfactory.tow.entities.File;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends CrudRepository<File, UUID> {

    @Query(value = "select * from file_db where filename = :filename and user_uuid = :userUUID LIMIT 1")
    Optional<File> findByFilenameAndUserUUID(String filename,UUID userUUID);
}
