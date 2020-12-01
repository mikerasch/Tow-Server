package edu.uwp.appfactory.tow.queryinterfaces;

import org.springframework.beans.factory.annotation.Value;


public interface VerifyTokenInterface {
    @Value("#{target.id}")
    String getId();

    @Value("#{target.email}")
    String getEmail();

    @Value("#{target.verify_token}")
    String getVerifyToken();

    @Value("#{target.verify_date}")
    String getVerifyDate();

    @Value("#{target.ver_enabled}")
    Boolean getVerEnabled();
}
