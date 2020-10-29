package edu.uwp.appfactory.tow.WebSecurityConfig.payload.response;

/**
 * JWT response class
 */
public class JwtResponse {

    /**
     * JWT Token
     */
    private String token;

    /**
     * type of token
     */
    private String type = "Bearer";

    /**
     * uuid of user
     */
    private String id;

    /**
     * username of user
     */
    private String username;

    /**
     * email of user
     */
    private String email;

    /**
     * firstname of user
     */
    private String firstname;

    /**
     * last name of user
     */
    private String lastname;

    /**
     * role of user
     */
    private final String role;

    /**
     * constructor for a JWT response, used mostly with login
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
     * getter
     * @return String token
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * setter
     * @param accessToken of user
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /**
     * getter
     * @return String token type
     */
    public String getTokenType() {
        return type;
    }

    /**
     * setter
     * @param tokenType of user
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    /**
     * getter
     * @return String id of user
     */
    public String getId() {
        return id;
    }

    /**
     * setter
     * @param id of user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter
     * @return String of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter
     * @param email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter
     * @return String of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter
     * @param username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter
     * @return String of firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * setter
     * @param firstname of user
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * getter
     * @return String of lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * setter
     * @param lastname of user
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * getter
     * @return String of role
     */
    public String getRole() {
        return role;
    }
}
