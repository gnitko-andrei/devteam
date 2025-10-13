package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.SecurityConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Tag("web")
@WebMvcTest(MainController.class)
@Import(SecurityConfiguration.class)
class MainControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderHome_whenGetRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void shouldRenderHomeMoreInfo_whenGetInfoProject() throws Exception {
        mockMvc.perform(get("/info/project"))
                .andExpect(status().isOk())
                .andExpect(view().name("homeMoreInfo"));
    }

    @Test
    void shouldRenderStackMoreInfo_whenGetInfoStack() throws Exception {
        mockMvc.perform(get("/info/stack"))
                .andExpect(status().isOk())
                .andExpect(view().name("stackMoreInfo"));
    }
}