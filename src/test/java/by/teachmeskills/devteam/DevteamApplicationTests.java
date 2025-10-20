package by.teachmeskills.devteam;

import by.teachmeskills.devteam.common.MySqlContainerSupport;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("smoke")
@SpringBootTest
class DevteamApplicationTests extends MySqlContainerSupport {

    @Test
    void contextLoads() {
        // Verifies that the Spring Boot application context starts successfully.
    }

}
