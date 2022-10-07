package edu.uwp.appfactory.tow.services.roles;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users findById(UUID userId) {
        Optional<Users> user = usersRepository.findById(userId);
        return user.orElse(null);
    }

    //todo: do not lock people out if they haven't verified, give them like a week to do it, then lock or delete
    //todo: ask client / zaid
    public Users updateByUUID(UUID userId, String firstname, String lastname, String email, String phone) {
        //todo: get user by uuid
        Optional<Users> usersOptional = usersRepository.findById(userId);

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
