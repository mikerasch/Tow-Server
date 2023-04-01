package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.Drivers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DriverRepositoryTest {
    @Autowired
    private DriverRepository driverRepository;

    @AfterEach
    void tearDown(){
        driverRepository.deleteAll();
    }

    @Test
    void itShouldCheckIfDriverExistsByEmail() {
        // given
        Drivers drivers = new Drivers(
                "testemail@gmail.com",
                "test",
                "1234123423",
                "hello",
                "goodbye",
                "222-222-2222",
                "ROLE_DRIVER",
                0F,
                0F
        );
        driverRepository.save(drivers);

        // when
        boolean wasFound = driverRepository.existsByEmail("testemail@gmail.com");

        // then
        assertThat(wasFound).isTrue();
    }

    @Test
    void itShouldCheckIfDriverDoesNotExistByEmail() {
        //given
        String doesNotExist = "idonotexist91234123@gmail.com";

        // when
        boolean wasFound = driverRepository.existsByEmail(doesNotExist);

        //then
        assertThat(wasFound).isFalse();
    }
}