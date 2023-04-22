package edu.uwp.appfactory.tow.mappers;

import edu.uwp.appfactory.tow.entities.PDUser;
import edu.uwp.appfactory.tow.responseobjects.PDUAuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PDMapper {
    PDUAuthResponse map(PDUser pdUser);
}
