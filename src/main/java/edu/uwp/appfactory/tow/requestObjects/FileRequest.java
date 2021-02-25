package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

@Value
public class FileRequest {
    String image;
    String userUUID;
    String name;
}