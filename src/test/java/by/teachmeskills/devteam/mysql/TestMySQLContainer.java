package by.teachmeskills.devteam.mysql;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestMySQLContainer extends MySQLContainer<TestMySQLContainer> {

    private static final TestMySQLContainer INSTANCE = new TestMySQLContainer();

    private TestMySQLContainer() {
        super(DockerImageName.parse("mysql:8.4.5"));
        withDatabaseName("devteam");
        withUsername("testuser");
        withPassword("1111");
    }

    public static TestMySQLContainer getInstance() {
        return INSTANCE;
    }

    @Override
    public void stop() { /* keep running till JVM exit */ }
}
