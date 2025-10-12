package by.teachmeskills.devteam.common.jpa;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

public class MySqlContainerSupport {
    @ServiceConnection
    protected static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4.5")
                    .withUsername("testuser")
                    .withPassword("secret123");
}
