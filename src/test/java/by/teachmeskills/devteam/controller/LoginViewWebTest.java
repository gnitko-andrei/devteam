package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.MvcConfig;
import by.teachmeskills.devteam.config.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
@Import({MvcConfig.class, SecurityConfiguration.class})
class LoginViewWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderLogin_whenGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldRedirectToLogin_whenGetAnyUrl_givenUnauthorisedUser() throws Exception {
        mockMvc.perform(get("/anyUrl"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
