package edu.uwp.appfactory.tow.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwp.appfactory.tow.controllers.superadmin.SuperAdminService;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.firebase.FirebaseMessagingService;
import edu.uwp.appfactory.tow.repositories.SuperAdminRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.SuperAdminRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private SuperAdminRepository superAdminRepository;

    @BeforeEach
    void tearDown(){
        superAdminRepository.deleteAll();
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
        addUserToDatabaseAndVerfiyEmail();
        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertEquals("application/json",response.getContentType());
    }

    @Test
    void testRefreshTokenGivenRealToken() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(
                "test@gmail.com",
                "dingledart123A!"
        );
        addUserToDatabaseAndVerfiyEmail();
        MockHttpServletResponse response = mockMvc.perform(
                        post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(loginRequest)))
                .andReturn().getResponse();
        String json = response.getContentAsString();
        JwtResponse jwtResponse = new ObjectMapper().readValue(json, JwtResponse.class);
        String token = jwtResponse.getToken();

        // when
        MockHttpServletResponse refreshResponse = mockMvc.perform(
                        post("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(loginRequest)).header("Authorization",token))
                .andReturn().getResponse();

        // then
        assertThat(refreshResponse.getStatus()).isEqualTo(200);
//        assertEquals("application/json",refreshResponse.getContentType());
    }

    private void addUserToDatabaseAndVerfiyEmail() {
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
        Optional<Users> user = usersRepository.findByEmail("test@gmail.com");
        usersRepository.updateUserEmailVerifiedByUUID(user.get().getId(),true);
    }


    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}