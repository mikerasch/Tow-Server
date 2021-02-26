package edu.uwp.appfactory.tow.requestObjects;

import lombok.Value;

import java.util.UUID;

@Value
public class FileRequest {
    String image;
    //    UUID userUUID;
//    String name;
    UUID id;
}