package edu.uwp.appfactory.tow.controllers.superadmin;

import edu.uwp.appfactory.tow.entities.*;
import edu.uwp.appfactory.tow.repositories.*;
<<<<<<< develop:src/main/java/edu/uwp/appfactory/tow/controllers/superadmin/SuperAdminService.java
import edu.uwp.appfactory.tow.requestobjects.rolerequest.SuperAdminRequest;
=======
import edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest.SuperAdminRequest;
>>>>>>> More intergration testing:src/main/java/edu/uwp/appfactory/tow/controllers/superAdmin/SuperAdminService.java
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.services.email.AsyncEmailService;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class SuperAdminService {
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AsyncEmailService sendEmail;
    private final TCAdminRepository tcAdminRepository;
    private final PDAdminRepository pdAdminRepository;
    private final UsersRepository usersRepository;
    private final TCUserRepository tcUserRepository;
    private final PDUserRepository pdUserRepository;
    private final DriverRepository driverRepository;

    public SuperAdminService(SuperAdminRepository superAdminRepository, PasswordEncoder passwordEncoder, AsyncEmailService sendEmail,
                             TCAdminRepository tcAdminRepository, PDAdminRepository pdAdminRepository, UsersRepository usersRepository,
                             TCUserRepository tcUserRepository, PDUserRepository pdUserRepository, DriverRepository driverRepository){
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.sendEmail = sendEmail;
        this.tcAdminRepository = tcAdminRepository;
        this.pdAdminRepository = pdAdminRepository;
        this.usersRepository = usersRepository;
        this.tcUserRepository = tcUserRepository;
        this.pdUserRepository = pdUserRepository;
        this.driverRepository = driverRepository;
    }

    public ResponseEntity<TestVerifyResponse> register(SuperAdminRequest spAdminRequest) {
        if(superAdminRepository.existsByEmail(spAdminRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already exists!");
        }
        List<String> passwordViolations = AccountInformationValidator.validatePassword(spAdminRequest.getPassword());
        if(!passwordViolations.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,passwordViolations.toString());
        }
        SPAdmin superAdmin = new SPAdmin(
                spAdminRequest.getFirstname(),
                spAdminRequest.getLastname(),
                spAdminRequest.getEmail(),
                passwordEncoder.encode(spAdminRequest.getPassword()),
                spAdminRequest.getPhone(),
                ERole.ROLE_SPADMIN.name(),
                spAdminRequest.getUsername()
        );
        superAdmin.setVerifyToken(generateEmailUUID());
        superAdmin.setVerifyDate(String.valueOf(LocalDate.now()));
        superAdmin.setVerEnabled(false);
        superAdminRepository.save(superAdmin);
        sendEmail.submitEmailExecution(superAdmin);
        TestVerifyResponse test = new TestVerifyResponse(superAdmin.getVerifyToken());
        return ResponseEntity.ok(test);
    }

    private String generateEmailUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public List<UsersDTO> getAllUsers() {
        List<Users> users = usersRepository.getAllUsers();
        List<UsersDTO> usersDTOS = new ArrayList<>();
        if(users.isEmpty()){
            return new ArrayList<>();
        }
        for(Users user: users){
            usersDTOS.add(
                    new UsersDTO(
                            user.getId(),
                            user.getEmail(),
                            user.getFirstname(),
                            user.getLastname(),
                            user.getRole(),
                            user.getUsername(),
                            user.getVerEnabled()
                    )
            );
        }
        return usersDTOS;
    }

    public ResponseEntity<HttpStatus> updateUser(UsersDTO usersDTO, String oldRole) {
        usersRepository.updateUser(
                usersDTO.getId(),
                usersDTO.getEmail(),
                usersDTO.getFirstname(),
                usersDTO.getLastname(),
                usersDTO.getRole(),
                usersDTO.getUsername(),
                usersDTO.getVerEnabled()
        );
        propagateAndDelete(usersDTO, ERole.valueOf(oldRole));
        return ResponseEntity.ok().build();
    }

    private void propagateAndDelete(UsersDTO usersDTO, ERole oldRole) {
        if(ERole.valueOf(usersDTO.getRole()).equals(oldRole)){
            return;
        }
        boolean userExistsInOldRole = doesExistInRole(usersDTO,oldRole);
        if(!userExistsInOldRole){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Users is not a " + oldRole);
        }
        String oldTableName = getTableName(oldRole);
        String newTableName = getTableName(ERole.valueOf(usersDTO.getRole()));
        if(oldTableName == null || newTableName == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Role Sent");
        }
        Optional<Users> user = usersRepository.findById(usersDTO.getId());
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not exist");
        }
        deleteFromTable(oldRole, usersDTO.getId());
        addToNewTable(usersDTO,user.get());
    }

    private boolean doesExistInRole(UsersDTO usersDTO, ERole oldRole) {
        return switch (oldRole) {
            case ROLE_TCADMIN -> tcAdminRepository.existsById(usersDTO.getId());
            case ROLE_TCUSER -> tcUserRepository.existsById(usersDTO.getId());
            case ROLE_PDADMIN -> pdAdminRepository.existsById(usersDTO.getId());
            case ROLE_PDUSER -> pdUserRepository.existsById(usersDTO.getId());
            case ROLE_DRIVER -> driverRepository.existsById(usersDTO.getId());
            case ROLE_SPADMIN -> superAdminRepository.existsById(usersDTO.getId());
            default -> false;
        };
    }

    private void addToNewTable(UsersDTO usersDTO, Users user) {
        String newRole = usersDTO.getRole();
        switch (newRole) {
            case "ROLE_TCADMIN" -> tcAdminRepository.save(new TCAdmin(
                    usersDTO.getEmail(),
                    usersDTO.getUsername(),
                    user.getPassword(),
                    usersDTO.getFirstname(),
                    usersDTO.getLastname(),
                    user.getPhone(),
                    usersDTO.getRole(),
                    null
            ));
            case "ROLE_TCUSER" -> tcUserRepository.save(new TCUser(
                    usersDTO.getEmail(),
                    usersDTO.getUsername(),
                    user.getPassword(),
                    usersDTO.getFirstname(),
                    usersDTO.getLastname(),
                    user.getPhone(),
                    usersDTO.getRole(),
                    null,
                    null,
                    false,
                    null

            ));
            case "ROLE_PDADMIN" -> pdAdminRepository.save(new PDAdmin(
                    usersDTO.getEmail(),
                    usersDTO.getUsername(),
                    user.getPassword(),
                    usersDTO.getFirstname(),
                    usersDTO.getLastname(),
                    user.getPhone(),
                    usersDTO.getRole(),
                    null,
                    0,
                    null,
                    null
            ));
            case "ROLE_PDUSER" -> pdUserRepository.save(new PDUser(
                    usersDTO.getEmail(),
                    usersDTO.getUsername(),
                    user.getPassword(),
                    usersDTO.getFirstname(),
                    usersDTO.getLastname(),
                    user.getPhone(),
                    usersDTO.getRole(),
                    null,
                    null
            ));
            case "ROLE_SPADMIN" -> superAdminRepository.save(new SPAdmin(
                    usersDTO.getFirstname(),
                    usersDTO.getLastname(),
                    usersDTO.getEmail(),
                    user.getPassword(),
                    user.getPhone(),
                    usersDTO.getRole(),
                    usersDTO.getUsername()
            ));
            default -> throw new IllegalArgumentException();
        }
        UUID newUUID = usersRepository.findIDByEmail(usersDTO.getEmail());
        usersRepository.updateUserEmailVerifiedByUUID(newUUID,true);
    }

    private void deleteFromTable(ERole oldRole, UUID id) {
        switch(oldRole){
            case ROLE_TCADMIN -> tcAdminRepository.deleteById(id);
            case ROLE_TCUSER -> tcUserRepository.deleteById(id);
            case ROLE_PDADMIN -> pdAdminRepository.deleteById(id);
            case ROLE_DRIVER -> driverRepository.deleteById(id);
            case ROLE_SPADMIN -> superAdminRepository.deleteById(id);
            default -> throw new IllegalArgumentException();
        }
    }

    private String getTableName(ERole oldRole) {
        return switch (oldRole) {
            case ROLE_TCADMIN -> "tc_admin";
            case ROLE_TCUSER -> "tc_user";
            case ROLE_PDADMIN -> "pd_admin";
            case ROLE_PDUSER -> "pd_user";
            case ROLE_DRIVER -> "drivers";
            case ROLE_SPADMIN -> "sp_admin";
            default -> null;
        };
    }

    public CountDTO getStatistics() {
        long pdCount = pdAdminRepository.count() + pdUserRepository.count();
        long tcCount = tcAdminRepository.count() + tcUserRepository.count();
        long totalUsers = usersRepository.count();
        return new CountDTO(
                pdCount,
                tcCount,
                totalUsers
        );
    }
}
