package edu.uwp.appfactory.tow.controllers.location;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;

import java.util.ArrayList;
import java.util.Collections;

import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * This class is used by both TC and PD user in order to store and the find by geodata.
 */
@Controller
@Slf4j
public class LocationService {
    private final TCUserRepository tcUserRepository;
    private final CalculateDistanceService calculateDistanceService;

    /**
     * Parameterized constructor for creating a new LocationService.
     * @param tcUserRepository - tow company user repository for management purposes
     */
    public LocationService(TCUserRepository tcUserRepository, CalculateDistanceService calculateDistanceService) {
        this.tcUserRepository = tcUserRepository;
        this.calculateDistanceService = calculateDistanceService;
    }

    /**
     * Method used by the tow truck users in order to continuously set their location.
     *
     * @param latitude the latitude of the tcuser
     * @param longitude the longitude of the tcuser
     * @return true if userUUID could be found, false otherwise
     */
    public boolean setLocation(float latitude, float longitude, UserDetailsImpl userDetails) {
        Optional<TCUser> tcUserOptional = tcUserRepository.findByEmail(userDetails.getEmail());
        if(tcUserOptional.isEmpty()){
            log.warn("Could not find TCUser email {}.", userDetails.getEmail());
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
     *
     * @param latitude the latitude of the accident site
     * @param longitude the longitude of the accident site
     * @param radius the radius of the search set by the pduser
     * @return returns a list of the tc users.
     */
    @Transactional
    public List<TCUser> findByDistance(float latitude, float longitude, int radius) {
        List<TCUser> allDrivers = StreamSupport.stream(tcUserRepository.findAll().spliterator(), false).toList();
        List<TCUser> nearestDrivers = findNearestDrivers(allDrivers, latitude, longitude, radius);
        if (!nearestDrivers.isEmpty()) {
            return nearestDrivers;
        }
        log.debug("Could not find any Tow Company drivers in the location requested");
        return Collections.emptyList();
    }

    /**
     * Finds all the TCUsers within a specified distance of a location.
     *
     * @param allDrivers A list of all TCUsers to search through.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param radius The radius of the search, in kilometers.
     * @return A list of TCUsers within the specified distance of the location.
     */
    private List<TCUser> findNearestDrivers(List<TCUser> allDrivers, float latitude, float longitude, int radius) {
        List<TCUser> tcUsersInDistance = new ArrayList<>();
        for(TCUser tcUser: allDrivers) {
            double distance = calculateDistanceService.calculateDistance(tcUser.getLatitude(), tcUser.getLongitude(), latitude, longitude);
            if(radius > distance) {
                tcUsersInDistance.add(tcUser);
            }
        }
        return tcUsersInDistance;
    }

    /**
     * Updates the active status of the current TCUser.
     *
     * @param parseBoolean The new active status of the TCUser.
     * @param userDetails The UserDetailsImpl of the currently authenticated user.
     * @return A ResponseEntity with a HttpStatus representing the success of the operation.
     *
     * @throws ResponseStatusException if the TCUser cannot be found in the database.
     */
    public ResponseEntity<HttpStatus> updateActiveStatus(boolean parseBoolean, UserDetailsImpl userDetails) {
        Optional<TCUser> tcUser = tcUserRepository.findByEmail(userDetails.getEmail());
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

    /**
     * Updates the active status of the current TCUser.
     *
     * @param parseBoolean The new active status of the TCUser.
     * @param userDetails The UserDetails of the currently authenticated user.
     */
    public void updateActiveStatus(boolean parseBoolean, UserDetails userDetails) {
        TCUser tcUser = tcUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user!"));
        tcUser.setActive(parseBoolean);
        tcUserRepository.save(tcUser);
    }
}
