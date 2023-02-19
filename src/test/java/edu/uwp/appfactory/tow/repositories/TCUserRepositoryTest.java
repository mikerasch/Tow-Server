package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.entities.TCUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TCUserRepositoryTest {
    @Autowired
    private TCUserRepository tcUserRepository;

    @AfterEach
    void tearDown(){
        tcUserRepository.deleteAll();
    }

    @Test
    void findListOfTCUsersGivenCoordsAndRadius() {
        //given
        List<TCUser> tcUsers = new ArrayList<>();
        List<String> validEmails = List.of("test1@gmail.com","test2@gmail.com","test3@gmail.com");
        tcUsers.add(new TCUser(
                "test1@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-2222",
                "ROLE_TCUSER",
                50F,
                100F,
                true,
                UUID.randomUUID()
        ));
        tcUsers.add(new TCUser(
                "test2@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-2222",
                "ROLE_TCUSER",
                40F,
                100F,
                true,
                UUID.randomUUID()
        ));
        tcUsers.add(new TCUser(
                "test3@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-2222",
                "ROLE_TCUSER",
                60F,
                100F,
                true,
                UUID.randomUUID()
        ));
        tcUsers.add(new TCUser(
                "test4@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-2222",
                "ROLE_TCUSER",
                5000F,
                1000F,
                true,
                UUID.randomUUID()
        ));
        tcUserRepository.saveAll(tcUsers);

        //when
        List<TCUser> tcUserList = tcUserRepository.findByDistance(50F,100F,30);
        List<String> emails = new ArrayList<>();
        tcUserList.forEach(x -> emails.add(x.getEmail()));
        //then
        assertEquals(3,tcUserList.size());
        assertTrue(emails.containsAll(validEmails));
        assertFalse(emails.contains("test4@gmail.com"));
    }
}