package by.teachmeskills.devteam.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcBuilderCustomizer;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@TestConfiguration
public class SecurityMockMvcConfig {

    @Bean
    public MockMvcBuilderCustomizer springSecurityMockMvcCustomizer() {
        return builder -> builder.apply(springSecurity());
    }
}
