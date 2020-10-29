/**
 * The Dispatcher entity interacts with the dispatcher table.
 */
package edu.uwp.appfactory.tow.entities;
import edu.uwp.appfactory.tow.data.IDispatcher;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity(name = "Dispatcher")
@Table(name = "dispatcher")
@PrimaryKeyJoinColumn(name="uuid")
public class Dispatcher extends Users implements IDispatcher {

    /**
     * creating a precinct string to hold the later value
     */
    @Column
    private String precinct;

    /**
     * Dispatcher constructor that creates a dispatcher with 6 parameters.
     * @param email dispatchers email
     * @param username dispatcher username
     * @param password dispatchers password
     * @param firstname dispatchers first name
     * @param lastname dispatchers last name
     * @param precinct dispatchers precinct
     */
    public Dispatcher(String email, String username, String password, String firstname, String lastname, String precinct) {
        super(email, username, password, firstname, lastname);
        this.precinct = precinct;
    }

    /**
     * default constructor
     */
    public Dispatcher() {

    }

    /**
     * getters and setters
     * @return
     */
    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }
}
