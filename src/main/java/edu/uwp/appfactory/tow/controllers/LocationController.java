package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is used by both TC and PD user in  order to store and the find by geodata.
 */
@Controller
public class LocationController {

    private final JwtUtils jwtUtils;
    private final TCUserRepository tcUserRepository;
    private final PDUserRepository pdUserRepository;

    public LocationController(JwtUtils jwtUtils, TCUserRepository tcUserRepository, PDUserRepository pdUserRepository) {
        this.jwtUtils = jwtUtils;
        this.tcUserRepository = tcUserRepository;
        this.pdUserRepository = pdUserRepository;
    }

    /**
     * Method used by the tow truck users in order to continuously set their location.
     * @param latitude the latitude of the tcuser
     * @param longitude the longitude of the tcuser
     * @param active a boolean that we need to set true when they are available for a tow and false when they are not.
     * @param userUUID the uuid od the user
     * @return
     */
    public boolean setLocation(float latitude, float longitude, boolean active, String userUUID) {
        Optional<TCUser> tcUserOptional = tcUserRepository.findById(UUID.fromString(userUUID));

        if (tcUserOptional.isPresent()) {
            TCUser tcUser = tcUserOptional.get();
            System.out.println(tcUser);
            tcUser.setLongitude(longitude);
            System.out.println(tcUser.getLongitude());
            tcUser.setLatitude(latitude);
            tcUser.setActive(active);
            System.out.println(tcUser.getActive());
            tcUserRepository.save(tcUser);
            return true;
        } else {
            return false;
        }
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
        if (drivers.size() != 0) {
            return drivers;
        } else {
            return null;
        }
    }
}
