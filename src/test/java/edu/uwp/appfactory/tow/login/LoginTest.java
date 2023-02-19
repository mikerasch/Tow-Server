package edu.uwp.appfactory.tow.login;

import edu.uwp.appfactory.tow.controllers.auth.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
public class LoginTest {

    @Autowired
    MockMvc mockMvc;
}
