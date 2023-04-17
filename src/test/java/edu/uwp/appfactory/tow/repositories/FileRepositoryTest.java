package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @AfterEach
    void tearDown(){
        fileRepository.deleteAll();
    }
    private Long uuid;

    @BeforeEach
    void initialize() throws IOException {
        File file = new File(
                1L,
                "JPG",
                "cool",
                Files.readAllBytes(Path.of("src/test/resources/newparkside.png")),
                new Timestamp(new Date().getTime()),
                "test"
        );
        this.uuid = file.getUserId();
        fileRepository.save(file);
    }

    @Test
    void itShouldCheckIfFileExistsWhenAdded() throws IOException {
        // given initialize()

        // when
        Optional<File> wasFoundByName = fileRepository.findByFilename("cool");
        boolean wasFoundByNameBoolean = wasFoundByName.isPresent();

        //then
        assertTrue(wasFoundByNameBoolean);
    }

    @Test
    void itShouldCheckIfExistsByFilenameAndUUID() throws IOException {
        // given initialize()

        // when
        Optional<File> wasFoundByNameAndUUID = fileRepository.findByFilenameAndUserId("cool",uuid);
        boolean wasFound = wasFoundByNameAndUUID.isPresent();

        // then
        assertTrue(wasFound);
    }
}