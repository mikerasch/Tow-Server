package edu.uwp.appfactory.tow.controllers.driver;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwp.appfactory.tow.repositories.DriverRepository;
import edu.uwp.appfactory.tow.requestobjects.rolerequest.DriverRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverService driverService;

    @BeforeEach
    void tearDown(){
        driverRepository.deleteAll();
    }
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DriverController(driverService)).build();
    }

    @Test
    void testCreatingNewDriver() throws Exception {
        // given
        DriverRequest driverRequest = new DriverRequest(
                "yafasdf",
                "testest",
                "testest2@gmail.com",
                "dingledart123A!",
                "222-2222-2222"
        );


        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/drivers/").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(driverRequest)))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertEquals("application/json",response.getContentType());
    }


    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}