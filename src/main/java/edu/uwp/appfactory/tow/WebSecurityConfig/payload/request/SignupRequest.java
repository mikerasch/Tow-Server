package edu.uwp.appfactory.tow.WebSecurityConfig.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 *
 */
public class SignupRequest {
    /**
     *
     */
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    /**
     *
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     *
     */
    private Set<String> role;

    /**
     *
     */
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public Set<String> getRole() {
        return this.role;
    }

    /**
     *
     * @param role
     */
    public void setRole(Set<String> role) {
        this.role = role;
    }
}
