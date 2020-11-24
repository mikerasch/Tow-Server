package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestObjects.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@EnableAutoConfiguration
@Controller
public class UserController {
    private final UsersRepository usersRepository;

    @Autowired
    public UserController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users findByUUID(String userUUID) {
        Optional<Users> user = usersRepository.findByUUID(userUUID);
        return user.orElse(null);
    }

    //todo: do not lock people out if they haven't verified, give them like a week to do it, then lock or delete
    //todo: ask client / zaid
    public Users updateByUUID(String userUUID, String firstname, String lastname, String email, String phone) {
        //todo: get user by uuid
        Optional<Users> usersOptional = usersRepository.findByUUID(userUUID);

        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setEmail(email);
            user.setPhone(phone);
            usersRepository.save(user);
            return user;
        } else
            return null;
    }

    public boolean deleteByEmail(String email) {
        Optional<Users> userOpt = usersRepository.findByUsername(email);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            usersRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }
}
