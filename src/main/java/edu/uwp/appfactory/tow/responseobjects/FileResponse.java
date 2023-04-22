package edu.uwp.appfactory.tow.responseobjects;

import lombok.Value;

@Value
public class FileResponse {
    String type;
    byte[] data;
}
