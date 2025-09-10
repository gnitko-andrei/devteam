package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.SecurityConfiguration;
import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeveloperController.class)
@Import(SecurityConfiguration.class)
class DeveloperControllerWebTest {

    public static final long USER_ID_1 = 1L;
    private static final UserDto USER_DTO_1 = UserDto.builder()
            .id(USER_ID_1)
            .build();
    private static final UserDto USER_DTO_2 = UserDto.builder()
            .id(2L)
            .build();
    private static final UserDto USER_DTO_3 = UserDto.builder()
            .id(3L)
            .build();

    @MockitoBean
    private UserService userServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "MANAGER")
    void shouldRenderDevelopersList_whenGetDevelopers() throws Exception {
        // given
        final var expectedDevelopers = List.of(USER_DTO_1, USER_DTO_2, USER_DTO_3);
        when(userServiceMock.getAllUsersByRole(any())).thenReturn(expectedDevelopers);
        // when / then
        mockMvc.perform(get("/developers"))
                .andExpect(status().isOk())
                .andExpect(view().name("developersList"))
                .andExpect(model().attribute("developers", expectedDevelopers));
        verify(userServiceMock).getAllUsersByRole(Role.DEVELOPER);
    }

    @Test
    @WithMockUser(authorities = "DEVELOPER")
    void shouldForbidAccess_whenGetDevelopers_givenNotManagerUser() throws Exception {
        // given
        // when / then
        mockMvc.perform(get("/developers"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(userServiceMock);
    }

    @Test
    @WithMockUser(authorities = "MANAGER")
    void shouldUpdateDeveloperRateAndRedirectToDevelopers_whenPostDevelopers_givenDeveloperIdAndPriceParams() throws Exception {
        // given
        var givenPrice = 100;
        // when / then
        mockMvc.perform(post("/developers").with(csrf())
                .param("id", String.valueOf(USER_ID_1))
                .param("price", String.valueOf(givenPrice)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/developers"));
        verify(userServiceMock).updateDeveloperRate(USER_ID_1, givenPrice);
    }

    @Test
    @WithMockUser(authorities = "MANAGER")
    void shouldDeleteDeveloper_whenDeleteDevelopers_givenDeveloperIdParam() throws Exception {
        // given
        // when / then
        mockMvc.perform(delete("/developers").with(csrf())
                .param("id", String.valueOf(USER_ID_1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/developers"));
        verify(userServiceMock).deleteById(USER_ID_1);
    }
}