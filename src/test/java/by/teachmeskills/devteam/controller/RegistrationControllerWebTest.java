package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.SecurityConfiguration;
import by.teachmeskills.devteam.config.SecurityMockMvcConfig;
import by.teachmeskills.devteam.dto.user.UserRegistrationDto;
import by.teachmeskills.devteam.service.UserService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("web")
@WebMvcTest(RegistrationController.class)
@Import({SecurityConfiguration.class, SecurityMockMvcConfig.class})
class RegistrationControllerWebTest {

    @MockitoBean
    private UserService userServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderRegistration_whenGetRegistration() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    void shouldCreateNewUserAndRedirectToLogin_whenPostRegistration_givenUserCreationDataInParamsAndUsernameIsFree() throws Exception {
        // given
        when(userServiceMock.isUserExists(any())).thenReturn(false);
        var givenUsername = "alice";
        var givenPassword = "pw";
        var givenFirstName = "Alice";
        var givenLastName = "A";
        var givenEmail = "alice@mail.com";
        var expectedUserRegistrationDto = UserRegistrationDto.builder()
                .username(givenUsername)
                .password(givenPassword)
                .firstName(givenFirstName)
                .lastName(givenLastName)
                .email(givenEmail)
                .build();
        // then / when
        mockMvc.perform(post("/registration").with(csrf())
                        .param("username", givenUsername)
                        .param("password", givenPassword)
                        .param("firstName", givenFirstName)
                        .param("lastName", givenLastName)
                        .param("email", givenEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userServiceMock).isUserExists(givenUsername);
        verify(userServiceMock).createNewUser(expectedUserRegistrationDto);
    }

    @Test
    void shouldNotCreateNewUserAndRedirectToRegistration_whenPostRegistration_givenAlreadyUsedUsername() throws Exception {
        // given
        when(userServiceMock.isUserExists(any())).thenReturn(true);
        var givenUsername = "alice";
        // then / when
        mockMvc.perform(post("/registration").with(csrf())
                        .param("username", givenUsername))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration"))
                .andExpect(flash().attribute("errorMessage", "User already exists!"));
        verify(userServiceMock).isUserExists(givenUsername);
        verify(userServiceMock, never()).createNewUser(any());
    }
}
