package edu.uwp.appfactory.tow.webSecurityConfig.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uwp.appfactory.tow.entities.SuperAdmin;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.webSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.webSecurityConfig.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * user details implementation, implementing userDetails
 */
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    final UUID id;
    private final String username;
    private final String email;
    @JsonIgnore
    private final String password;
    private final String role;
    private String firstname;
    private String lastname;
    private final String phone;

    /**
     * constructor of a user details implementation
     */
    public UserDetailsImpl(UUID id, String username, String email, String password, String firstname, String lastname,
                           String role, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.phone = phone;
    }

    /**
     * build method for a user details implementation
     */
    public static UserDetailsImpl buildUser(Users user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole(),
                user.getPhone());
    }

    public static UserDetailsImpl buildSuperAdmin(SuperAdmin superAdmin){
        return new UserDetailsImpl(
                superAdmin.getId(),
                superAdmin.getUsername(),
                superAdmin.getEmail(),
                superAdmin.getPassword(),
                superAdmin.getFirstname(),
                superAdmin.getLastname(),
                superAdmin.getRole(),
                superAdmin.getPhone()
        );
    }
    public String getRole() {
        return role;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    /**
     * getter for authorities
     *
     * @return collection of authorities of a user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ERole eRole = switch (getRole()) {
            case "ROLE_PDADMIN" -> ERole.ROLE_PDADMIN;
            case "ROLE_PDUSER" -> ERole.ROLE_PDUSER;
            case "ROLE_TCADMIN" -> ERole.ROLE_TCADMIN;
            case "ROLE_TCUSER" -> ERole.ROLE_TCUSER;
            case "ROLE_SPADMIN" -> ERole.ROLE_SPADMIN;
            default -> ERole.ROLE_ADMIN;
        };

        Role tempRole = new Role();
        tempRole.setName(eRole);
        Collection<Role> roles = new ArrayList<>();
        roles.add(tempRole);

        return roles.stream()
                .map(role1 -> new SimpleGrantedAuthority(role1.getName().name()))
                .collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
