package edu.uwp.appfactory.tow.repositories;

import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.securityconfig.repository.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TCUserRepositoryTest {
    @Autowired
    private TCUserRepository tcUserRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private UsersRepository usersRepository;

    @AfterEach
    void tearDown(){
        usersRepository.deleteAll();
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
                "222-222-2219",
                "ROLE_TCUSER",
                42.718891F,
                -87.789401F,
                true
        ));
        tcUsers.add(new TCUser(
                "test2@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-2220",
                "ROLE_TCUSER",
                42.590926F,
                -87.843468F,
                true
        ));
        tcUsers.add(new TCUser(
                "test3@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-2221",
                "ROLE_TCUSER",
                42.513029F,
                -88.110065F,
                true
        ));
        tcUsers.add(new TCUser(
                "test4@gmail.com",
                "test",
                "1234567",
                "test1",
                "tester1",
                "222-222-1283",
                "ROLE_TCUSER",
                41.860197F,
                -87.689484F,
                true
        ));
        tcUserRepository.saveAll(tcUsers);

        //when
        List<TCUser> tcUserList = locationService.findByDistance(42.718476F, -87.845744F, 50);
        List<String> emails = new ArrayList<>();
        tcUserList.forEach(x -> emails.add(x.getEmail()));
        //then
        assertEquals(3,tcUserList.size());
        assertTrue(emails.containsAll(validEmails));
        assertFalse(emails.contains("test4@gmail.com"));
    }
}