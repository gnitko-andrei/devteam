package by.teachmeskills.devteam.common.jpa;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
public abstract class AbstractJpaTest {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4.5")
            .withDatabaseName("devteam")
            .withUsername("testuser")
            .withPassword("1111");

    @BeforeAll
    static void logConnectionInfo() {
        System.out.println("🐳 Testcontainer JDBC URL: " + MYSQL.getJdbcUrl());
        System.out.println("User: " + MYSQL.getUsername());
        System.out.println("Pass: " + MYSQL.getPassword());
    }
}
