package at.fhtw.swen3;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {BaseIntegrationTest.Initializer.class})
@TestPropertySource("/integration-tests.properties")
@Testcontainers
abstract class BaseIntegrationTest {
    @Container
    public static JdbcDatabaseContainer postgis = new PostgisContainerProvider().newInstance("15-3.3-alpine");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgis.getJdbcUrl(),
                    "spring.datasource.username=" + postgis.getUsername(),
                    "spring.datasource.password=" + postgis.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @LocalServerPort
    protected int port;
}
