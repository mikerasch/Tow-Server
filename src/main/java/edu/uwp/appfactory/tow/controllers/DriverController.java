package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;


@EnableAutoConfiguration
@Controller
public class DriverController {

    private final UsersRepository usersRepository;

    @Autowired
    public DriverController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

}
