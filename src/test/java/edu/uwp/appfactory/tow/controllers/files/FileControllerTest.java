package edu.uwp.appfactory.tow.controllers.files;

import edu.uwp.appfactory.tow.controllers.auth.AuthService;
import edu.uwp.appfactory.tow.controllers.policedepartment.PDAdminService;
import edu.uwp.appfactory.tow.entities.Users;
import edu.uwp.appfactory.tow.repositories.FileRepository;
import edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest.LoginRequest;
import edu.uwp.appfactory.tow.controllers.requestObjects.rolerequest.PDAdminRequest;
import edu.uwp.appfactory.tow.webSecurityConfig.payload.response.JwtResponse;
import edu.uwp.appfactory.tow.webSecurityConfig.repository.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PDAdminService pdAdminService;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private FileRepository fileRepository;
    private String token;
    @BeforeEach
    void setUp() {
        PDAdminRequest pdAdmin = new PDAdminRequest(
                "test",
                "hello",
                "testemail@gmail.com",
                "dingledart123A!",
                "222-222-2222",
                "hello",
                123,
                "department",
                "d"
        );
        pdAdminService.register(pdAdmin);
        Optional<Users> user = usersRepository.findByEmail("testemail@gmail.com");
        usersRepository.updateUserEmailVerifiedByUUID(user.get().getId(),true);
        ResponseEntity<JwtResponse> response = authService.authenticateUser(new LoginRequest(pdAdmin.getEmail(), pdAdmin.getPassword()));
        token = response.getBody().getToken();
    }

    @AfterEach
    void tearDown() {
        usersRepository.deleteAll();
        fileRepository.deleteAll();
    }
    @Test
    void uploadFileTest() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cool.jpg",
                MediaType.IMAGE_PNG_VALUE,
                Files.readAllBytes(Path.of("src/test/resources/newparkside.png"))
        );
        String urlName = "/files/download/cool.jpg";

        // when
        MockHttpServletResponse response = mockMvc.perform(multipart("/files").file(file).header("Authorization",token))
                .andReturn().getResponse();

        MockHttpServletResponse responseGetting = mockMvc.perform(get(urlName).header("Authorization",token)).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(responseGetting.getStatus()).isEqualTo(200);
        assertThat(responseGetting.getContentType()).isEqualTo("image/png");
    }
}