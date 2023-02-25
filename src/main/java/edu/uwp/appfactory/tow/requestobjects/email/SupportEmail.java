package edu.uwp.appfactory.tow.requestobjects.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportEmail {
    private String senderEmail;
    private String subject;
    private String message;
}
