package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.FileDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileDBRepository extends CrudRepository<FileDB, UUID> {
}
