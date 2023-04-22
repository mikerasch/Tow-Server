package edu.uwp.appfactory.tow.securityconfig.security.services;

import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import edu.uwp.appfactory.tow.securityconfig.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * user details service implementation
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UsersRepository userRepository;
    SuperAdminRepository superAdminRepository;
    public UserDetailsServiceImpl(UsersRepository userRepository, SuperAdminRepository superAdminRepository){
        this.userRepository = userRepository;
        this.superAdminRepository = superAdminRepository;
    }
    /**
     * load a user by their username, exception if not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDetailsImpl.buildUser(user);
    }

    /**
     * load a user by their username, exception if not found
     */
    public UserDetails loadUserByUUID(Long id) {
        Optional<Users> user = userRepository.findById(id);
        return user.map(UserDetailsImpl::buildUser).orElse(null);
    }
}
