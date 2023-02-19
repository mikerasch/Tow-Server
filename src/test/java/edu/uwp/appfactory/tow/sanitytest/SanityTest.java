package edu.uwp.appfactory.tow.sanitytest;
import edu.uwp.appfactory.tow.TowApplication;
import edu.uwp.appfactory.tow.controllers.auth.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(classes = {TowApplication.class})
class SanityTest {


    @Test
    void contextLoads(){
    }
}
