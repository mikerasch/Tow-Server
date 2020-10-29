package edu.uwp.appfactory.tow.WebSecurityConfig.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.Role;
import edu.uwp.appfactory.tow.entities.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * user details implementation, implementing userDetails
 */
public class UserDetailsImpl implements UserDetails {
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * UUID of a user
     */
    private final String UUID;

    /**
     * username of a user
     */
    private final String username;

    /**
     * email of a user
     */
    private final String email;

    /**
     * password of a user
     */
    @JsonIgnore
    private final String password;

    /**
     * firstname of a user
     */
    private String firstname;

    /**
     * lastname of a user
     */
    private String lastname;

    /**
     * role of a user
     */
    private final String role;

    /**
     * constructor of a user details implementation
     * @param UUID user uuid
     * @param username user username
     * @param email user email
     * @param password user password
     * @param firstname user firstname
     * @param lastname user lastname
     * @param role user role
     */
    public UserDetailsImpl(String UUID, String username, String email, String password, String firstname, String lastname,
                           String role) {
        this.UUID = UUID;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    /**
     * build method for a user details implementation
     * @param user user object
     * @return userDetails object
     */
    public static UserDetailsImpl build(Users user) {
        return new UserDetailsImpl(
                user.getUUID(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getRoles());
    }

    /**
     * getter for role
     * @return user role
     */
    public String getRole() {
        return role;
    }

    /**
     * getting for a uuid
     * @return user uuid
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * getter for email
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * getter for authorities
     * @return collection of authorities of a user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ERole eRole;

        if (getRole().equals("ROLE_DRIVER")) {
            eRole = ERole.ROLE_DRIVER;
        } else if (getRole().equals("ROLE_DISPATCHER")) {
            eRole = ERole.ROLE_DISPATCHER;
        } else {
            eRole = ERole.ROLE_ADMIN;
        }

        Role tempRole = new Role();

        tempRole.setName(eRole);

        Collection<Role> roles = new ArrayList<>();

        roles.add(tempRole);

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return authorities;
    }

    /**
     *
     * @return
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(UUID, user.UUID);
    }

    /**
     *
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     *
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
