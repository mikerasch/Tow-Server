package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @AfterEach
    void tearDown(){
        fileRepository.deleteAll();
    }
    private UUID uuid;

    @BeforeEach
    void initialize() throws IOException {
        UUID second = UUID.randomUUID();
        File file = new File(
                second,
                "JPG",
                "cool",
                Files.readAllBytes(Path.of("src/test/resources/newparkside.png"))
        );
        this.uuid = file.getUser_uuid();
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
        Optional<File> wasFoundByNameAndUUID = fileRepository.findByFilenameAndUserUUID("cool",uuid);
        boolean wasFound = wasFoundByNameAndUUID.isPresent();

        // then
        assertTrue(wasFound);
    }
}