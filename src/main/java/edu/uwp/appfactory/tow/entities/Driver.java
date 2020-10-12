package edu.uwp.appfactory.tow.entities;

import edu.uwp.appfactory.tow.data.IDriver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "Driver")
@Table(name = "driver")
@PrimaryKeyJoinColumn(name= "user_uuid")
public class Driver extends Users implements IDriver {

    @Column
    private String business;

    @Column
    private String cdlLicenceNumber;

    public Driver(String email, String username, String password, String firstname, String lastname, String business, String cdlLicenceNumber) {
        super(email, username, password, firstname, lastname);
        this.business = business;
        this.cdlLicenceNumber = cdlLicenceNumber;
    }

    public Driver() {

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
