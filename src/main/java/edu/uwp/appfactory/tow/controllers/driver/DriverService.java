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

    /**
     * Registers a new driver account with the provided information.
     * Validates the email and password, and creates a new driver record in the database.
     * Sends a verification email to the new user.
     *
     * @param driverRequest the request object containing the driver's information.
     * @return a ResponseEntity containing a TestVerifyResponse object with the verification token.
     * @throws ResponseStatusException if the email is invalid or already exists, or the password is weak.
     */
    public ResponseEntity<TestVerifyResponse> register(DriverRequest driverRequest) {
        if(!AccountInformationValidator.validateEmail(driverRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Typo in email");
        }
        List<String> errorPassword = AccountInformationValidator.validatePassword(driverRequest.getPassword());
        if(!errorPassword.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorPassword.toString());
        }

        if (!driverRepository.existsByEmail(driverRequest.getEmail())){
            Drivers drivers = new Drivers (
                    driverRequest.getEmail(),
                    driverRequest.getEmail(),
                    encoder.encode(driverRequest.getPassword()),
                    driverRequest.getFirstname(),
                    driverRequest.getLastname(),
                    driverRequest.getPhone(),
                    ERole.ROLE_DRIVER.name()
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
     *
     * @return UUID converted to a String
     */
    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Updates the location of the driver in the database with the provided coordinates.
     *
     * @param coordinates the new coordinates of the driver
     * @param user the authenticated user making the request
     * @return a ResponseEntity with an HTTP status code of OK if the update was successful
     * @throws ResponseStatusException if the user making the request is not a driver or if the user could not be found in the database
     */
    public ResponseEntity<HttpStatus> updateLocation(Coordinates coordinates, UserDetailsImpl user) {
        Optional<Drivers> driversOptional = driverRepository.findByEmail(user.getEmail());
        if(driversOptional.isEmpty()){
            log.error("Driver tried updating location, but user could not be found in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");
        }
        Drivers driver = driversOptional.get();
        driver.setLatitude(coordinates.getLatitude());
        driver.setLongitude(coordinates.getLongitude());
        driverRepository.save(driver);
        log.debug("Changing coordinates of driver {} with initial coordinates of {} : {} to {} : {}", user.getEmail(), driver.getLatitude(), driver.getLongitude(), coordinates.getLatitude(), coordinates.getLatitude());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Finds tow truck drivers available within a given radius of the current driver's location.
     * Sends a notification to the nearest available driver to assist the current driver.
     *
     * @param radius the radius within which to search for available tow truck drivers
     * @param user the current driver making the request
     * @return a ResponseEntity with HttpStatus.OK if an available tow truck driver was found and notified,
     *         or with HttpStatus.CONFLICT if no available tow truck driver was found within the specified radius
     * @throws ResponseStatusException if the current driver could not be found in the database
     */
    public ResponseEntity<HttpStatus> findTowTruckDrivers(int radius, UserDetailsImpl user) {
        Optional<Drivers> driversOptional = driverRepository.findByEmail(user.getEmail());
        if(driversOptional.isEmpty()){
            log.error("Driver tried finding tow truck, but user could not be found in database.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found!");
        }
        Drivers drivers = driversOptional.get();
        List<TCUser> tcUsersAvailable = locationService.findByDistance (
                drivers.getLatitude(),
                drivers.getLongitude(),
                radius
        );
        List<TCUser> ensureAllDriversAreNotRepeats = generateUniqueDriver(tcUsersAvailable,user.getId());
        if(ensureAllDriversAreNotRepeats.isEmpty()){
            log.info("Driver tried finding tow trucks, but no tow trucks are currently on duty");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        storeRequest(ensureAllDriversAreNotRepeats.get(0),user.getId());
        firebaseMessagingService.sendNotificationToTowTruck(drivers,ensureAllDriversAreNotRepeats);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * This method stores the request details for a tow truck driver and a driver requesting tow truck services in the database.
     *
     * @param tcUser The TCUser object representing the tow truck driver who received the request.
     * @param id The driver's ID requesting tow truck services.
     */
    private void storeRequest(TCUser tcUser, Long id) {
        Requests requests = new Requests();
        requests.setDriverId(id);
        requests.setTowTruckId(tcUser.getId());
        requests.setRequestDateTime(Timestamp.valueOf(LocalDateTime.now()));
        requestRepository.save(requests);
    }

    /**
     * Generates a list of unique drivers from a given list of available TCUsers by removing
     * any TCUser who is already assigned to a tow truck for the given driver ID.
     *
     * @param tcUsersAvailable the list of available TCUsers to generate unique drivers from
     * @param id the ID of the driver to check for tow truck assignments
     * @return a list of unique drivers from the available TCUsers
     */
    private List<TCUser> generateUniqueDriver(List<TCUser> tcUsersAvailable, Long id) {
        List<Requests> requestsList = requestRepository.findAllByDriverId(id);
        for(Requests requests: requestsList) {
            Long towTruckUUID = requests.getTowTruckId();
            tcUsersAvailable.removeIf(tcUser -> tcUser.getId().equals(towTruckUUID));
        }
        return tcUsersAvailable;
    }
}
