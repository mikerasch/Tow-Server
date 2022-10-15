package edu.uwp.appfactory.tow.services.locator;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import java.util.Collections;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is used by both TC and PD user in order to store and the find by geodata.
 */
@Controller
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
     * @param active a boolean that we need to set true when they are available for a tow and false when they are not.
     * @param userUUID the uuid od the user
     * @return true if userUUID could be found, false otherwise
     */
    public boolean setLocation(float latitude, float longitude, boolean active, String userUUID) {
        Optional<TCUser> tcUserOptional = tcUserRepository.findById(UUID.fromString(userUUID));
        if(tcUserOptional.isEmpty()){
            return false;
        }
        TCUser tcUser = tcUserOptional.get();
        tcUser.setLongitude(longitude);
        tcUser.setLatitude(latitude);
        tcUser.setActive(active);
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
        return Collections.emptyList();
    }
}
