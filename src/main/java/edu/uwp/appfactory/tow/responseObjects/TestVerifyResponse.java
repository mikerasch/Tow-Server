package edu.uwp.appfactory.tow.responseObjects;

import lombok.Value;

@Value
public class TestVerifyResponse {
    String token;

    @Override
    public String toString(){
        return token;
    }
}
