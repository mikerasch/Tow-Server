package edu.uwp.appfactory.tow.applicationrunner;

import edu.uwp.appfactory.tow.entities.SPAdmin;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminInit implements ApplicationRunner {
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${SP_EMAIL}")
    private String email;
    @Value("${SP_PASSWORD}")
    private String password;
    @Value("${SP_PHONE")
    private String phone;
    public SuperAdminInit(SuperAdminRepository superAdminRepository, PasswordEncoder passwordEncoder) {
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!superAdminRepository.existsByEmail(email)) {
            SPAdmin spAdmin = new SPAdmin(
                    "super admin",
                    "super admin",
                    email,
                    passwordEncoder.encode(password),
                    phone,
                    "ROLE_SPADMIN",
                    "spadmin"
            );
            spAdmin.setVerEnabled(true);
            superAdminRepository.save(spAdmin);
        }
    }
}
