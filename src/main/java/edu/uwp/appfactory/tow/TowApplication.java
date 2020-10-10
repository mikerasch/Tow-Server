package edu.uwp.appfactory.tow;

import edu.uwp.appfactory.tow.testingEntities.DispatcherUsers;
import edu.uwp.appfactory.tow.testingEntities.DriverUsers;
import edu.uwp.appfactory.tow.testingEntities.Users;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TowApplication.class, args);
    }
    DispatcherUsers x = new DispatcherUsers("sick@sick.com",
            "sick@sick.com",
            "cool",
            "john",
            "doe",
            "lit");
}
