package edu.uwp.appfactory.tow.WebSecurityConfig.payload.response;

/**
 * message response class to be used when returning a message to a request
 */
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
