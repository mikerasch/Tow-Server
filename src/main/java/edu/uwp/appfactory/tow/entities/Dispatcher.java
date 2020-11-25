package edu.uwp.appfactory.tow.entities;

import edu.uwp.appfactory.tow.data.IDispatcher;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * The Dispatcher entity interacts with the dispatcher table.
 */
@Entity(name = "Dispatcher")
@Table(name = "dispatcher")
@PrimaryKeyJoinColumn(name="uuid")
public class Dispatcher extends Users implements IDispatcher {

    @Column
    private String precinct;

    /**
     * Dispatcher constructor that creates a dispatcher with 6 parameters.
     */
    public Dispatcher(String email, String username, String password, String firstname, String lastname, String phone, String precinct) {
        super(email, username, password, firstname, lastname, phone);
        this.precinct = precinct;
    }

    /**
     * Default
     */
    public Dispatcher() { }

    public String getPrecinct() {
        return precinct;
    }
    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }
}
