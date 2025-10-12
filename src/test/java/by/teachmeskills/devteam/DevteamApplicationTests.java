package by.teachmeskills.devteam;

import by.teachmeskills.devteam.common.jpa.MySqlContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DevteamApplicationTests extends MySqlContainerSupport {

    @Test
    void contextLoads() {
        // Verifies that the Spring Boot application context starts successfully.
    }

}
