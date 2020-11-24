package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.repositories.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

@EnableAutoConfiguration
@Controller
public class DispatcherController {

    private final DispatcherRepository dispatcherRepository;

    @Autowired
    public DispatcherController(DispatcherRepository dispatcherRepository) {
        this.dispatcherRepository = dispatcherRepository;
    }
}
