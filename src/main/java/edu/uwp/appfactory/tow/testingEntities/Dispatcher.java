package edu.uwp.appfactory.tow.testingEntities;

import edu.uwp.appfactory.tow.testingData.IDispatcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "DispatcherUsers")
@Table(name = "dispatcher")
@PrimaryKeyJoinColumn(name="role_uuid")
public class Dispatcher extends Users implements IDispatcher {

    @Column
    private String precinct;

    public Dispatcher(String email, String username, String password, String firstname, String lastname, String precinct) {
        super(email, username, password, firstname, lastname);
        this.precinct = precinct;
    }

    public Dispatcher() {

    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }
}
