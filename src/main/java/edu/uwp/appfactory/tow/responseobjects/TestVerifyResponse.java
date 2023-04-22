package edu.uwp.appfactory.tow.responseobjects;

import lombok.Value;

@Value
public class TestVerifyResponse {
    String token;

    @Override
    public String toString(){
        return token;
    }
}
