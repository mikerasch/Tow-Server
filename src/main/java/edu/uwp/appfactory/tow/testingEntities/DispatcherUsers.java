package edu.uwp.appfactory.tow.testingEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "DispatcherUsers")
@Table(name = "dispatcher")
@PrimaryKeyJoinColumn(name="role_uuid")
public class DispatcherUsers extends Users {

    @Column
    private String precinct;

    public DispatcherUsers(String email, String username, String password, String firstname, String lastname, String precinct) {
        super(email, username, password, firstname, lastname);
        this.precinct = precinct;
    }

    public DispatcherUsers() {

    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }
}
