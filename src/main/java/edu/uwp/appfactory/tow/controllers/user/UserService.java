package edu.uwp.appfactory.tow.controllers.user;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestobjects.password.PasswordChange;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.UpdateRequest;
import edu.uwp.appfactory.tow.requestobjects.users.UsersDTO;
import edu.uwp.appfactory.tow.securityconfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
        return usersRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    public UsersDTO updateByUUID(UpdateRequest updateRequest, UserDetailsImpl userDetails) {
        if(!updateRequest.getPhone().equals(userDetails.getPhone())) {
            checkForDuplicates(usersRepository.findByPhone(updateRequest.getPhone()), "Phone is already in use.");
        }

        if(!updateRequest.getEmail().equals(userDetails.getEmail())) {
            checkForDuplicates(usersRepository.findByEmail(updateRequest.getEmail()), "Email is already in use.");
        }

        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));

        user.setFirstname(updateRequest.getFirstname());
        user.setLastname(updateRequest.getLastname());
        user.setEmail(updateRequest.getEmail());
        user.setPhone(updateRequest.getPhone());
        usersRepository.save(user);
        return new UsersDTO(
                user.getId(),
                user.getEmail(),
                user.getEmail(),
                user.getLastname(),
                user.getRole(),
                user.getUsername(),
                user.getVerEnabled()
        );
    }

    private void checkForDuplicates(Optional<Users> optionalUser, String errorMessage) {
        if(optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
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

        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
        user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
        usersRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean passwordMatches(PasswordChange passwordChange) {
        return passwordChange.getNewPassword().equals(passwordChange.getNewPasswordConfirmation());
    }
}
