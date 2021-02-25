package edu.uwp.appfactory.tow.mappers;

import edu.uwp.appfactory.tow.entities.TCAdmin;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestVerifyResponse map(TCAdmin tcAdmin);
}
