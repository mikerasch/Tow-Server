package edu.uwp.appfactory.tow.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwp.appfactory.tow.controllers.superAdmin.SuperAdminService;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.requestObjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import edu.uwp.appfactory.tow.webSecurityConfig.security.jwt.AuthTokenFilter;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private SuperAdminService superAdminService;
    @AfterEach
    void tearDown(){
      //  usersRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();
    }

    @Test
    void testLoginToGetJwtToken() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(
                "test@gmail.com",
                "dingledart123A!"
        );
        addUserToDatabase();
        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    private void addUserToDatabase() {
        superAdminService.register(
                new SuperAdminRequest(
                        "test@gmail.com",
                        "test",
                        "test@gmail.com",
                        "dingledart123A!",
                        "222-222-2222",
                        "mike"
                )
        );
    }


    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}