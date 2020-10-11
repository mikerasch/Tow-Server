package edu.uwp.appfactory.tow;

import edu.uwp.appfactory.tow.testingEntities.Dispatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TowApplication.class, args);
    }
    Dispatcher x = new Dispatcher("sick@sick.com",
            "sick@sick.com",
            "cool",
            "john",
            "doe",
            "lit");
}
