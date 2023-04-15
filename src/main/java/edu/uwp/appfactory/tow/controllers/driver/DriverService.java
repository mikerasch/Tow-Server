package edu.uwp.appfactory.tow.controllers.driver;

import edu.uwp.appfactory.tow.controllers.location.LocationService;
import edu.uwp.appfactory.tow.entities.Drivers;
import edu.uwp.appfactory.tow.entities.Requests;
import edu.uwp.appfactory.tow.entities.TCUser;
import edu.uwp.appfactory.tow.firebase.FirebaseMessagingService;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import edu.uwp.appfactory.tow.repositories.RequestRepository;
import edu.uwp.appfactory.tow.requestobjects.location.Coordinates;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.DriverRequest;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.controllers.email.AsyncEmailService;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class DriverService {
    private final DriverRepository driverRepository;
    private final PasswordEncoder encoder;

    private final AsyncEmailService asyncEmailService;

    private final LocationService locationService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final RequestRepository requestRepository;
    public DriverService(DriverRepository driverRepository, PasswordEncoder passwordEncoder, AsyncEmailService asyncEmailService, LocationService locationService, FirebaseMessagingService firebaseMessagingService,
                         RequestRepository requestRepository) {
        this.driverRepository = driverRepository;
        this.encoder = passwordEncoder;
        this.asyncEmailService = asyncEmailService;
        this.locationService = locationService;
        this.firebaseMessagingService = firebaseMessagingService;
        this.requestRepository = requestRepository;
    }
    public ResponseEntity<TestVerifyResponse> register(DriverRequest driverRequest) {
        if(!AccountInformationValidator.validateEmail(driverRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Typo in email");
        }
        List<String> errorPassword = AccountInformationValidator.validatePassword(driverRequest.getPassword());
        if(!errorPassword.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorPassword.toString());
        }

        // todo remove latitude and longitude from this request
        if (!driverRepository.existsByEmail(driverRequest.getEmail())){
            Drivers drivers = new Drivers(
                    driverRequest.getEmail(),
                    driverRequest.getEmail(),
                    encoder.encode(driverRequest.getPassword()),
                    driverRequest.getFirstname(),
                    driverRequest.getLastname(),
                    driverRequest.getPhone(),
                    ERole.ROLE_DRIVER.name(),
                    0F,
                    0F
            );
            drivers.setVerifyToken(generateEmailUUID());
            drivers.setVerifyDate(String.valueOf(LocalDate.now()));
            drivers.setVerEnabled(false);
            driverRepository.save(drivers);
            asyncEmailService.submitSignupEmailExecution(drivers);
            TestVerifyResponse testVerifyResponse = new TestVerifyResponse(drivers.getVerifyToken());
            log.debug("Saving new user with role {} and email {}", drivers.getRole(), drivers.getEmail());
            return ResponseEntity.ok(testVerifyResponse);
        }
        log.warn("While adding a new driver: email {} already existed", driverRequest.getEmail());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exists!");
    }

    /**
     * Generates a random 6 character length UUID.
     * @return UUID converted to a String
     */
    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public ResponseEntity<HttpStatus> updateLocation(Coordinates coordinates, UserDetailsImpl user) {
        Optional<Drivers> driversOptional = driverRepository.findById(user.getId());
        if(driversOptional.isEmpty()){
            log.error("Driver tried updating location, but user could not be found in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");
        }
        Drivers driver = driversOptional.get();
        driver.setLatitude(coordinates.getLatitude());
        driver.setLongitude(coordinates.getLongitude());
        driverRepository.save(driver);
        log.debug("Changing coordinates of driver {} with initial coordinates of {} : {} to {} : {}", driver.getEmail(), driver.getLatitude(), driver.getLongitude(), coordinates.getLatitude(), coordinates.getLatitude());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> findTowTruckDrivers(int radius, UserDetailsImpl user) {
        Optional<Drivers> driversOptional = driverRepository.findById(user.getId());
        if(driversOptional.isEmpty()){
            log.error("Driver tried finding tow truck, but user could not be found in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");
        }
        Drivers drivers = driversOptional.get();
        List<TCUser> tcUsersAvailable = locationService.findByDistance(
                drivers.getLatitude(),
                drivers.getLongitude(),
                radius
        );
        List<TCUser> ensureAllDriversAreNotRepeats = generateUniqueDriver(tcUsersAvailable,drivers.getId());
        if(ensureAllDriversAreNotRepeats.isEmpty()){
            log.info("Driver tried finding tow trucks, but no tow trucks are currently on duty");
            return new ResponseEntity<>(HttpStatus.CONFLICT); // todo should not be conflict
        }
        storeRequest(ensureAllDriversAreNotRepeats.get(0),drivers.getId());
        firebaseMessagingService.sendNotificationToTowTruck(drivers,ensureAllDriversAreNotRepeats);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void storeRequest(TCUser tcUser, UUID id) {
        Requests requests = new Requests();
        requests.setDriver_id(id);
        requests.setTow_truck_id(tcUser.getId());
        requests.setRequest_date(Timestamp.valueOf(LocalDateTime.now()));
        requestRepository.save(requests);
    }

    private List<TCUser> generateUniqueDriver(List<TCUser> tcUsersAvailable, UUID id) {
        List<Requests> requestsList = requestRepository.findAllByDriverId(id);
        for(Requests requests: requestsList) {
            UUID towTruckUUID = requests.getTow_truck_id();
            tcUsersAvailable.removeIf(tcUser -> tcUser.getId().equals(towTruckUUID));
        }
        return tcUsersAvailable;
    }
}
