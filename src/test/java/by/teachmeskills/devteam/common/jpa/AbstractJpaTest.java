package by.teachmeskills.devteam.common.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
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
