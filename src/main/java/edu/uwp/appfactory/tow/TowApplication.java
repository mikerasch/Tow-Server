package edu.uwp.appfactory.tow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * main application class, contains the main method
 */
@EnableScheduling
@SpringBootApplication
public class TowApplication {
    /**
     * main method, starts the server
     * @param args: arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TowApplication.class, args);
    }

}
