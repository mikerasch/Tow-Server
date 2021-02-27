package edu.uwp.appfactory.tow.responseObjects;

import lombok.Value;

@Value
public class FileResponse {
    String type;
    byte[] data;
}
