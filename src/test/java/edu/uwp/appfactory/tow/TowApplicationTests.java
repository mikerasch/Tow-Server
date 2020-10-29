package edu.uwp.appfactory.tow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TowApplicationTests {

    @Test
    void contextLoads() {
        assertThat("Hello").isEqualTo("Hello");
    }

}
