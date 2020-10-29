/**
 * The DispatcherController class communicates between the dispatchers route and the repositories.
 * There are multiple methods contained in this class.
 */

package edu.uwp.appfactory.tow.controllers;
import edu.uwp.appfactory.tow.repositories.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;


@EnableAutoConfiguration
@Controller
public class DispatcherController {

    /**
     * Creating a new dispatcher repository object.
     */
    private final DispatcherRepository dispatcherRepository;

    /**
     * DispatcherController constructor that news up a dispatch
     * controller and sets the repo equal to the current repo.
     * @param dispatcherRepository set the dispatchrepo to match the current repo
     */
    @Autowired
    public DispatcherController(DispatcherRepository dispatcherRepository) {
        this.dispatcherRepository = dispatcherRepository;
    }

}
