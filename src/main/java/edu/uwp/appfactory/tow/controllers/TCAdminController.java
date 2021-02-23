package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.repositories.TCAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;


@Controller
public class TCAdminController {

    private final TCAdminRepository tcAdminRepository;

    @Autowired
    public TCAdminController(TCAdminRepository tcAdminRepository) {
        this.tcAdminRepository = tcAdminRepository;
    }
}
