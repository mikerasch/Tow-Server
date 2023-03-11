package edu.uwp.appfactory.tow.controllers.user;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UpdateRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Find a user by their ID.
     * @return User if UUID exists, null otherwise
     */
    public Users findById(UserDetailsImpl userDetails) {
        Optional<Users> user = Optional.of(usersRepository.findById(userDetails.getId()).orElseThrow());
        return user.get();
    }

    //todo: do not lock people out if they haven't verified, give them like a week to do it, then lock or delete
    //todo: ask client / zaid
    public Users updateByUUID(UpdateRequest updateRequest, UserDetailsImpl userDetails) {
        //todo: get user by uuid
        Optional<Users> usersOptional = Optional.of(usersRepository.findByEmail(userDetails.getEmail()).orElseThrow());
        Users user = usersOptional.get();
        user.setFirstname(updateRequest.getFirstname());
        user.setLastname(updateRequest.getLastname());
        user.setEmail(updateRequest.getEmail());
        user.setPhone(updateRequest.getPhone());
        user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        usersRepository.save(user);
        return user;
    }

    /**
     * Delete a user from the database given there email.
     * @param email - email address of user to delete
     * @return true if delete was successful, false otherwise
     */
    public boolean deleteByEmail(String email) {
        Optional<Users> userOpt = usersRepository.findByUsername(email);
        if(userOpt.isEmpty()){
            return false;
        }
        Users user = userOpt.get();
        usersRepository.delete(user);
        return true;
    }
}
