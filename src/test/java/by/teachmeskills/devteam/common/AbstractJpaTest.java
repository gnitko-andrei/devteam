package by.teachmeskills.devteam.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Tag("jpa")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(FlywayAutoConfiguration.class)
public abstract class AbstractJpaTest extends MySqlContainerSupport {

    @BeforeEach
    void printConn() {
        System.out.printf("🐳 mysql host=localhost port=%d db=%s user=%s password=%s%n",
                mysql.getMappedPort(3306), mysql.getDatabaseName(), mysql.getUsername(), mysql.getPassword());
        String url = "jdbc:mysql://localhost:" + mysql.getMappedPort(3306) + "/" + mysql.getDatabaseName();
        System.out.println("🐳 jdbc url:            " + url);
        System.out.println("🐳 jdbc url (no-ssl):   " + url + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC\n");
    }
}
