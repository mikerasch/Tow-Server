package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.repositories.PDAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

@EnableAutoConfiguration
@Controller
public class PDAdminController {

    private final PDAdminRepository pdAdminRepository;

    @Autowired
    public PDAdminController(PDAdminRepository pdAdminRepository) {
        this.pdAdminRepository = pdAdminRepository;
    }
}
