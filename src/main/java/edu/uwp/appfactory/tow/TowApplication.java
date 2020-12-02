package edu.uwp.appfactory.tow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * main application class, contains the main method
 * @author: Gianluca Eickenberg, Colin Hoffman, Quincy Kayle, Matthew Rank
*/

@EnableScheduling
@SpringBootApplication
public class  TowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TowApplication.class, args);
    }
}
