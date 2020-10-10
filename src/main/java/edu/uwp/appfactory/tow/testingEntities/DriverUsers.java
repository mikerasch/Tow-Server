package edu.uwp.appfactory.tow.testingEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "DriverUsers")
@Table(name = "driver")
@PrimaryKeyJoinColumn(name= "role_uuid")
public class DriverUsers extends Users {

    @Column
    private String business;

    @Column
    private String cdlLicenceNumber;

    public DriverUsers(String email, String username, String password, String firstname, String lastname, String business, String cdlLicenceNumber) {
        super(email, username, password, firstname, lastname);
        this.business = business;
        this.cdlLicenceNumber = cdlLicenceNumber;
    }

    public DriverUsers() {

    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public void setCdlLicenceNumber(String cdlLicenceNumber) {
        this.cdlLicenceNumber = cdlLicenceNumber;
    }

    public String getBusiness() {
        return business;
    }

    public String getCdlLicenceNumber() {
        return cdlLicenceNumber;
    }
}
