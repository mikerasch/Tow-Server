package edu.uwp.appfactory.tow.mappers;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.responseObjects.TCAdminResponse;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TCMapper {
    TCAdminResponse map(TCAdmin tcAdmin);

    // will automatically detect and convert
    default UUID map(String uuid) {
        return UUID.fromString(uuid);
    }
}
