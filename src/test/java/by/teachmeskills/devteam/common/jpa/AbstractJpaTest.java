package by.teachmeskills.devteam.common.jpa;

import by.teachmeskills.devteam.mysql.TestMySQLContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
public abstract class AbstractJpaTest {

    private static final TestMySQLContainer MYSQL = TestMySQLContainer.getInstance();
    static { MYSQL.start(); }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", MYSQL::getJdbcUrl);
        r.add("spring.datasource.username", MYSQL::getUsername);
        r.add("spring.datasource.password", MYSQL::getPassword);
    }

    @BeforeAll
    static void log() {
        System.out.println("🐳 Testcontainer JDBC URL: " + MYSQL.getJdbcUrl());
        System.out.println("User: " + MYSQL.getUsername());
        System.out.println("Pass: " + MYSQL.getPassword());
    }
}
