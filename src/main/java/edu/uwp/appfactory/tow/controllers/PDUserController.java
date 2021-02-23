package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;


@Controller
public class PDUserController {

    private final PDUserRepository pdUserRepository;

    @Autowired
    public PDUserController(PDUserRepository pdUserRepository) {
        this.pdUserRepository = pdUserRepository;
    }
}
