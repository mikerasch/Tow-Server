package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;


@Controller
public class TCUserController {

    private final TCUserRepository tcUserRepository;

    @Autowired
    public TCUserController(TCUserRepository tcUserRepository) {
        this.tcUserRepository = tcUserRepository;
    }
}
