package edu.uwp.appfactory.tow.repositories;
import org.springframework.core.io.Resource;
import edu.uwp.appfactory.tow.entities.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Repository
public interface FileRepository extends CrudRepository<File, UUID> {

}
