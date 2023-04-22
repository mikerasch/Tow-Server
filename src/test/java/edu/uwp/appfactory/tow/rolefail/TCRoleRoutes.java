package edu.uwp.appfactory.tow.rolefail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.uwp.appfactory.tow.controllers.towcompany.TCAdminController;
import edu.uwp.appfactory.tow.controllers.towcompany.TCAdminService;
import edu.uwp.appfactory.tow.controllers.towcompany.TCUserService;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.*;
import edu.uwp.appfactory.tow.securityconfig.security.jwt.JwtUtils;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(TCAdminController.class)
class TCRoleRoutes {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TCAdminService tcAdminService;
    @MockBean
    JwtUtils jwtUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private TCUserService tcUserService;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    void registerTCAdminShouldReturn505() throws Exception {
        TCAdminRequest tcAdminRequest = new TCAdminRequest(
                "test",
                "hello",
                "testhello@gmail.com",
                "123451234123432",
                "222-222-2222",
                "testing"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String putJson = objectWriter.writeValueAsString(tcAdminRequest);
        mockMvc.perform(put("/tcadmins/").contentType(APPLICATION_JSON_UTF8)
                .content(putJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerTcUserShouldReturn500() throws Exception {
        TCUserRequest tcUserRequest = new TCUserRequest(
                "test",
                "hello",
                "testhello@gmail.com",
                "1234512341234123",
                "222-222-2222",
                50,
                30,
                20
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String putJson = objectWriter.writeValueAsString(tcUserRequest);
        mockMvc.perform(put("/tcusers/").contentType(APPLICATION_JSON_UTF8)
                .content(putJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerPDAdminShouldReturn500() throws Exception {
        PDAdminRequest pdAdminRequest = new PDAdminRequest(
                "test",
                "hello",
                "testhello@gmail.com",
                "123412345612341234",
                "222-222-2222",
                "tester",
                12,
                "hello",
                "h"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String putJson = objectWriter.writeValueAsString(pdAdminRequest);
        mockMvc.perform(put("/pdadmins/").contentType(APPLICATION_JSON_UTF8)
                .contentType(putJson))
                .andExpect(status().isForbidden());
    }
    @Test
    void registerPDUserShouldReturn500() throws Exception {
        PDUserRequest pdUserRequest = new PDUserRequest(
                "test",
                "hello",
                "testhello@gmail.com",
                "123412341423"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String putJson = objectWriter.writeValueAsString(pdUserRequest);
        mockMvc.perform(put("/pdusers/").contentType(APPLICATION_JSON_UTF8)
                        .contentType(putJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerSuperAdminShouldReturn500() throws Exception {
        SuperAdminRequest superAdminRequest = new SuperAdminRequest(
                "test",
                "hello",
                "testhello@gmail.com",
                "1234123512432",
                "222-222-2222",
                "testhello"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String putJson = objectWriter.writeValueAsString(superAdminRequest);
        mockMvc.perform(put("/spadmins/").contentType(APPLICATION_JSON_UTF8)
                        .contentType(putJson))
                .andExpect(status().isForbidden());
    }
}
