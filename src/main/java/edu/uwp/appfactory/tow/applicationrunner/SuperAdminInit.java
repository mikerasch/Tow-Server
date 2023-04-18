package edu.uwp.appfactory.tow.applicationrunner;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminInit implements ApplicationRunner {
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;
    public SuperAdminInit(SuperAdminRepository superAdminRepository, PasswordEncoder passwordEncoder) {
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!superAdminRepository.existsByEmail("spadmin123@gmail.com")) {
            SPAdmin spAdmin = new SPAdmin(
                    "super admin",
                    "super admin",
                    "spadmin123@gmail.com",
                    passwordEncoder.encode("dingledart123A!"),
                    "555-555-5555",
                    "ROLE_SPADMIN",
                    "spadmin"
            );
            spAdmin.setVerEnabled(true);
            superAdminRepository.save(spAdmin);
        }
    }
}
