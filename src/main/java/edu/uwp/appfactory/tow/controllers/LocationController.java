package edu.uwp.appfactory.tow.controllers;

import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.repositories.PDUserRepository;
import edu.uwp.appfactory.tow.repositories.TCUserRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.JwtUtils;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.UUID;


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

//    public List<PDUser> findByDistance(float latitude, float longitude, int radius) {
//        List<PDUser> drivers = pdUserRepository.findByDistance(latitude, longitude, radius);
//        if (drivers.size() != 0) {
//            return drivers;
//        } else {
//            return null;
//        }
//    }
}
