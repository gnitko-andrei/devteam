package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.MvcConfig;
import by.teachmeskills.devteam.config.SecurityConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("web")
@WebMvcTest(controllers = SecurityFilterChainWebTest.TestPostController.class)
@Import({MvcConfig.class, SecurityConfiguration.class, SecurityFilterChainWebTest.TestPostController.class})
class SecurityFilterChainWebTest {

    @Controller
    public static class TestPostController {
        @PostMapping("/test/post")
        @ResponseBody
        public String post() { return "ok"; }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderLogin_whenGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithAnonymousUser
    void shouldRedirectToLogin_whenGetAnyUrl_givenUnauthorisedUser() throws Exception {
        mockMvc.perform(get("/anyUrl"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void shouldReturnForbidden_whenPostWithoutCsrf() throws Exception {
        mockMvc.perform(post("/test/post"))                // no token
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/test/post").with(csrf().useInvalidToken()))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/test/post").with(csrf()))   // with token
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void shouldLogoutAndRedirectToLogin() throws Exception {
        mockMvc.perform(logout())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }

}
