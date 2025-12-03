package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.SecurityConfiguration;
import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.dto.user.UserProfileUpdateDto;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.exception.WrongPasswordException;
import by.teachmeskills.devteam.service.UserService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("web")
@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
class UserControllerWebTest {

    public static final long USER_ID_1 = 1L;
    public static final User USER_1 = User.builder()
            .id(USER_ID_1)
            .build();

    @MockitoBean
    private UserService userServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderUserProfile_whenGetUser_givenUserIdFromAuthenticationPrincipal() throws Exception {
        // given
        var userDtoMock = mock(UserDto.class);
        when(userServiceMock.findById(any())).thenReturn(userDtoMock);
        // when / then
        mockMvc.perform(get("/user").with(user(USER_1)))
                .andExpect(status().isOk())
                .andExpect(view().name("userProfile"))
                .andExpect(model().attribute("currentUser", userDtoMock));
        verify(userServiceMock).findById(USER_ID_1);
    }

    @Test
    void shouldRenderUserEditor_whenGetUserEditor_givenUserIdFromAuthenticationPrincipal() throws Exception {
        // given
        var userDtoMock = mock(UserDto.class);
        when(userServiceMock.findById(any())).thenReturn(userDtoMock);
        // when / then
        mockMvc.perform(get("/user/userEditor").with(user(USER_1)))
                .andExpect(status().isOk())
                .andExpect(view().name("userEditor"))
                .andExpect(model().attribute("user", userDtoMock));
        verify(userServiceMock).findById(USER_ID_1);
    }

    @Test
    void shouldRedirectToUser_whenPostUserEditor_givenUserIdFromAuthenticationPrincipalAndDataToUpdateInParamsAndPasswordIsCorrect() throws Exception {
        // given
        var givenCurrentPassword = "currentPassword";
        var givenNewPassword = "newPassword";
        var givenFirstName = "firstName";
        var givenLastName = "lastName";
        var givenEmail = "email";
        var givenContacts = "contacts";
        var givenSkills = "skills";
        var expectedUserProfileUpdateDto = UserProfileUpdateDto.builder()
                .currentPassword(givenCurrentPassword)
                .newPassword(givenNewPassword)
                .firstName(givenFirstName)
                .lastName(givenLastName)
                .email(givenEmail)
                .contacts(givenContacts)
                .skills(givenSkills)
                .build();
        // when / then
        mockMvc.perform(post("/user/userEditor").with(csrf())
                        .with(user(USER_1))
                        .param("currentPassword", givenCurrentPassword)
                        .param("newPassword", givenNewPassword)
                        .param("firstName", givenFirstName)
                        .param("lastName", givenLastName)
                        .param("email", givenEmail)
                        .param("contacts", givenContacts)
                        .param("skills", givenSkills)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));
        verify(userServiceMock).updateUserProfile(USER_ID_1, expectedUserProfileUpdateDto);
    }


    @Test
    void shouldNotUpdateUserAndRedirectToUserEditor_whenPostUserEditor_givenWrongPassword() throws Exception {
        // given
        doThrow(new WrongPasswordException()).when(userServiceMock).updateUserProfile(any(), any());
        var givenCurrentPassword = "currentPassword";
        var expectedUserProfileUpdateDto = UserProfileUpdateDto.builder()
                .currentPassword(givenCurrentPassword)
                .build();
        // when / then
        mockMvc.perform(post("/user/userEditor").with(csrf())
                        .with(user(USER_1))
                        .param("currentPassword", givenCurrentPassword)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/userEditor"))
                .andExpect(flash().attribute("errorMessage", "Wrong current password!"));
        verify(userServiceMock).updateUserProfile(USER_ID_1, expectedUserProfileUpdateDto);
    }

    @Test
    void shouldDeleteUserAndLogout_whenDeleteUser() throws Exception {
        // given
        // when / then
        mockMvc.perform(delete("/user").with(csrf()).with(user(USER_1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userServiceMock).deleteById(USER_ID_1);
    }
}