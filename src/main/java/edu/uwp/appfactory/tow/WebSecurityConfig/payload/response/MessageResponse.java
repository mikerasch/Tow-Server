package edu.uwp.appfactory.tow.WebSecurityConfig.payload.response;

/**
 * message response class to be used when returning a message to a request
 */
public class MessageResponse {
    /**
     * the message
     */
    private String message;

    /**
     * constructor for the message
     * @param message sent in
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    /**
     * getter for message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * setter for the message
     * @param message sent in
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
