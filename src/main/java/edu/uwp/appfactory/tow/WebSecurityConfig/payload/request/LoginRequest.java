package edu.uwp.appfactory.tow.WebSecurityConfig.payload.request;

import javax.validation.constraints.NotBlank;

/**
 *
 */
public class LoginRequest {
    /**
     *
     */
    @NotBlank
    private String username;

    /**
     *
     */
    @NotBlank
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
}
