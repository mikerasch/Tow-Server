package edu.uwp.appfactory.tow.mappers;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestMapper {
    @Mapping(target = "token", ignore = true)
    TestVerifyResponse map(TCAdmin tcAdmin);
}
