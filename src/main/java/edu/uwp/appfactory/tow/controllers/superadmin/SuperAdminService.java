package edu.uwp.appfactory.tow.controllers.superadmin;

import edu.uwp.appfactory.tow.entities.*;
import edu.uwp.appfactory.tow.repositories.*;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.requestobjects.statistics.CountDTO;
import edu.uwp.appfactory.tow.requestobjects.users.UsersDTO;
import edu.uwp.appfactory.tow.responseObjects.TestVerifyResponse;
import edu.uwp.appfactory.tow.controllers.email.AsyncEmailService;
import edu.uwp.appfactory.tow.utilities.AccountInformationValidator;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            log.info("Registering super admin failed due to duplicate email found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email already exists!");
        }
        List<String> passwordViolations = AccountInformationValidator.validatePassword(spAdminRequest.getPassword());
        if(!passwordViolations.isEmpty()){
            log.debug("Registering super admin failed due to password violations found: {}", passwordViolations);
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
        log.debug("Saved new super admin to database");
        sendEmail.submitSignupEmailExecution(superAdmin);
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
            log.info("While propagating user to new role table, user already belongs to this role {}", oldRole.name());
            return;
        }
        boolean userExistsInOldRole = doesExistInRole(usersDTO,oldRole);
        if(!userExistsInOldRole){
            log.warn("While propagating user to new role, user does not exist in old table User ID: {}", usersDTO.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Users is not a " + oldRole);
        }
        String oldTableName = getTableName(oldRole);
        String newTableName = getTableName(ERole.valueOf(usersDTO.getRole()));
        if(oldTableName == null || newTableName == null) {
            log.warn("While propagating user to a new role, role could not be verified. Possible typo in {} or {}", oldRole.name(), usersDTO.getRole());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Role Sent");
        }
        Optional<Users> user = usersRepository.findById(usersDTO.getId());
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not exist");
        }
        deleteFromTable(oldRole, usersDTO.getEmail());
        addToNewTable(usersDTO,user.get());
    }
    @Transactional
    private boolean doesExistInRole(UsersDTO usersDTO, ERole oldRole) {
        return switch (oldRole) {
            case ROLE_TCADMIN -> tcAdminRepository.existsByEmail(usersDTO.getEmail());
            case ROLE_TCUSER -> tcUserRepository.existsByEmail(usersDTO.getEmail());
            case ROLE_PDADMIN -> pdAdminRepository.existsByEmail(usersDTO.getEmail());
            case ROLE_PDUSER -> pdUserRepository.existsByEmail(usersDTO.getEmail());
            case ROLE_DRIVER -> driverRepository.existsByEmail(usersDTO.getEmail());
            case ROLE_SPADMIN -> superAdminRepository.existsByEmail(usersDTO.getEmail());
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
                    0
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
            case "ROLE_DRIVER" -> driverRepository.save(new Drivers(
                    usersDTO.getEmail(),
                    usersDTO.getUsername(),
                    user.getPassword(),
                    usersDTO.getUsername(),
                    usersDTO.getLastname(),
                    user.getPhone(),
                    usersDTO.getRole()
            ));
            default -> throw new IllegalArgumentException();
        }
        Long id = usersRepository.findIDByEmail(usersDTO.getEmail());
        usersRepository.updateUserEmailVerifiedByUUID(id,true);
    }

    private void deleteFromTable(ERole oldRole, String email) {
        switch(oldRole){
            case ROLE_TCADMIN -> tcAdminRepository.deleteByEmail(email);
            case ROLE_TCUSER -> tcUserRepository.deleteByEmail(email);
            case ROLE_PDADMIN -> pdAdminRepository.deleteByEmail(email);
            case ROLE_DRIVER -> driverRepository.deleteByEmail(email);
            case ROLE_SPADMIN -> superAdminRepository.deleteByEmail(email);
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

    public ResponseEntity<HttpStatus> deleteUser(String email) {
        usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TC email not found!"));
        usersRepository.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
