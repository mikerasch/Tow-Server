package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SuperAdminRepositoryTest {
    @Autowired
    private SuperAdminRepository superAdminRepository;

    @AfterEach
    void teardown(){
        superAdminRepository.deleteAll();
    }

    @BeforeEach
    void initialize(){
        SPAdmin spAdmin = new SPAdmin(
                "test",
                "hello",
                "testhello@gmail.com",
                "125691234123",
                "222-222-2222",
                "ROLE_SPADMIN",
                "hello"
        );
        superAdminRepository.save(spAdmin);
    }

    @Test
    void itShouldCheckIfSuperAdminExistsByEmail() {
        //given initialize()

        // when
        boolean doesExist = superAdminRepository.existsByEmail("testhello@gmail.com");

        //then
        assertTrue(doesExist);
    }

    @Test
    void findByEmail() {
        // given initialize()

        // when
        Optional<SPAdmin> spAdminFound = superAdminRepository.findByEmail("testhello@gmail.com");

        // then
        assertTrue(spAdminFound.isPresent());
        assertEquals("testhello@gmail.com",spAdminFound.get().getEmail());
    }
}