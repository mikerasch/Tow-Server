package edu.uwp.appfactory.tow.controllers.user;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.password.PasswordChange;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UpdateRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static edu.uwp.appfactory.tow.utilities.AccountInformationValidator.validatePassword;


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

    //todo make sure email and phone number don't already exist in repo and add checksz
    public Users updateByUUID(UpdateRequest updateRequest, UserDetailsImpl userDetails) {
        Optional<Users> usersOptional = Optional.of(usersRepository.findByEmail(userDetails.getEmail()).orElseThrow());
        Users user = usersOptional.get();
        user.setFirstname(updateRequest.getFirstname());
        user.setLastname(updateRequest.getLastname());
        user.setEmail(updateRequest.getEmail());
        user.setPhone(updateRequest.getPhone());
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

    public ResponseEntity<HttpStatus> updatePassword(PasswordChange passwordChange, UserDetailsImpl userDetails) {
        if(!passwordMatches(passwordChange)) {
            return ResponseEntity.badRequest().build();
        }
        List<String> passwordValidationMatches = validatePassword(passwordChange.getNewPassword());
        if(!passwordValidationMatches.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passwordValidationMatches.toString());
        }

        Optional<Users> usersOptional = Optional.of(usersRepository.findByEmail(userDetails.getEmail()).orElseThrow());
        Users user = usersOptional.get();
        user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
        usersRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean passwordMatches(PasswordChange passwordChange) {
        return passwordChange.getNewPassword().equals(passwordChange.getNewPasswordConfirmation());
    }
}
