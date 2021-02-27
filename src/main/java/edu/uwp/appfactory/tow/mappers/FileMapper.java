package edu.uwp.appfactory.tow.mappers;

import edu.uwp.appfactory.tow.entities.File;
import edu.uwp.appfactory.tow.responseObjects.FileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResponse map(File file);
}
