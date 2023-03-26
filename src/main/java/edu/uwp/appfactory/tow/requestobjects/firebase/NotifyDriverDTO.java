package edu.uwp.appfactory.tow.requestobjects.firebase;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyDriverDTO {
    private String title;
    private String body;
    private String token;
    private String longitude;
    private String latitude;
    private String channel;
}
