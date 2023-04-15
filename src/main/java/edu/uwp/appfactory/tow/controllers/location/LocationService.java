package edu.uwp.appfactory.tow.controllers.location;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import java.util.Collections;

import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * This class is used by both TC and PD user in order to store and the find by geodata.
 */
@Controller
@Slf4j
public class LocationService {
    private final TCUserRepository tcUserRepository;

    /**
     * Parameterized constructor for creating a new LocationService.
     * @param tcUserRepository - tow company user repository for management purposes
     */
    public LocationService(TCUserRepository tcUserRepository) {
        this.tcUserRepository = tcUserRepository;
    }

    /**
     * Method used by the tow truck users in order to continuously set their location.
     * @param latitude the latitude of the tcuser
     * @param longitude the longitude of the tcuser
     * @return true if userUUID could be found, false otherwise
     */
    public boolean setLocation(float latitude, float longitude, UserDetailsImpl userDetails) {
        Optional<TCUser> tcUserOptional = tcUserRepository.findById(userDetails.getId());
        if(tcUserOptional.isEmpty()){
            log.warn("Could not find TCUser id {}.", userDetails.getId());
            return false;
        }
        TCUser tcUser = tcUserOptional.get();
        tcUser.setLongitude(longitude);
        tcUser.setLatitude(latitude);
        tcUserRepository.save(tcUser);
        return true;
    }

    /**
     * used by the police officer users to return a list of tcusers that are both within range and currently available to tow.
     * @param latitude the latitude of the accident site
     * @param longitude the longitude of the accident site
     * @param radius the radius of the search set by the pduser
     * @return returns a list of the tc users.
     */
    public List<TCUser> findByDistance(float latitude, float longitude, int radius) {
        List<TCUser> drivers = tcUserRepository.findByDistance(latitude, longitude, radius);
        if (!drivers.isEmpty()) {
            return drivers;
        }
        log.debug("Could not find any Tow Company drivers in the location requested");
        return Collections.emptyList();
    }

    public ResponseEntity<HttpStatus> updateActiveStatus(boolean parseBoolean, UserDetailsImpl userDetails) {
        Optional<TCUser> tcUser = tcUserRepository.findById(userDetails.getId());
        if(tcUser.isEmpty()) {
            log.warn("Could not find tow truck user upon trying to update active status. User ID: {}", userDetails.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");
        }
        TCUser user = tcUser.get();
        user.setActive(parseBoolean);
        log.debug("Setting User: {} active status to {}", userDetails.getId(), parseBoolean);
        tcUserRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public void updateActiveStatus(boolean parseBoolean, UserDetails userDetails) {
        Optional<TCUser> tcUser = Optional.of(tcUserRepository.findByEmail(userDetails.getUsername()).orElseThrow());
        TCUser user = tcUser.get();
        user.setActive(parseBoolean);
        tcUserRepository.save(user);
    }
}
