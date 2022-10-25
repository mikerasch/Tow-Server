package edu.uwp.appfactory.tow.webSecurityConfig.security.services;

import edu.uwp.appfactory.tow.entities.SuperAdmin;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * user details service implementation
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsersRepository userRepository;
    @Autowired
    SuperAdminRepository superAdminRepository;
    /**
     * load a user by their username, exception if not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return UserDetailsImpl.buildUser(user.get());
        }
        Optional<SuperAdmin> pdAdmin = superAdminRepository.findByEmail(username);
        if(pdAdmin.isEmpty()){
            throw new UsernameNotFoundException("Username is not found!");
        }
        return UserDetailsImpl.buildSuperAdmin(pdAdmin.get());
    }

    /**
     * load a user by their username, exception if not found
     */
    public UserDetails loadUserByUUID(UUID id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + id));
        return UserDetailsImpl.buildUser(user);
    }
}
