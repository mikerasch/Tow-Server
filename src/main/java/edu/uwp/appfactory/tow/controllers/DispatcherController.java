

package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.Role;
import edu.uwp.appfactory.tow.WebSecurityConfig.payload.response.MessageResponse;
import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.data.PDriver;
import edu.uwp.appfactory.tow.entities.Driver;
import edu.uwp.appfactory.tow.repositories.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolationException;
import java.util.List;


@EnableAutoConfiguration
@Controller
public class DispatcherController {



    private final DispatcherRepository dispatcherRepository;

    @Autowired
    public DispatcherController(DispatcherRepository dispatcherRepository) {
        this.dispatcherRepository = dispatcherRepository;
    }


}
