package edu.uwp.appfactory.tow.WebSecurityConfig.payload.response;

/**
 *
 */
public class JwtResponse {

    /**
     *
     */
    private String token;

    /**
     *
     */
    private String type = "Bearer";

    /**
     *
     */
    private String id;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private String firstname;

    /**
     *
     */
    private String lastname;

    /**
     *
     */
    private final String role;

    /**
     *
     * @param accessToken
     * @param id
     * @param username
     * @param email
     * @param firstname
     * @param lastname
     * @param role
     */
    public JwtResponse(String accessToken, String id, String username, String email, String
            firstname, String lastname, String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    /**
     *
     * @return
     */
    public String getAccessToken() {
        return token;
    }

    /**
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /**
     *
     * @return
     */
    public String getTokenType() {
        return type;
    }

    /**
     *
     * @param tokenType
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
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

    /**
     *
     * @return
     */
    public String getRole() {
        return role;
    }
}
