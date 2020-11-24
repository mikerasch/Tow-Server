package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.WebSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.queryinterfaces.UpdateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class UserController {
    private final UsersRepository usersRepository;

    @Autowired
    public UserController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users findByEmail(String email) {
        Optional<Users> user = usersRepository.findByUsername(email);
        return user.orElse(null);
    }

    //todo: do not lock people out if they havent verified, give them like a week to do it, then lock or delete
    public Users updateByUUID(String userUUID, UpdateUser userNew) {
        //todo: get user by uuid
        Optional<Users> usersOptional = usersRepository.findByUUID(userUUID);

        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            user.setFirstname(userNew.getFirstname());
            user.setLastname(userNew.getLastname());
            user.setEmail(userNew.getEmail());
            user.setPhone(userNew.getPhone());
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
