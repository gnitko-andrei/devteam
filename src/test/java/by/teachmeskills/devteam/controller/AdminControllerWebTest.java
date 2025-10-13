package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.SecurityConfiguration;
import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.dto.user.UserUpdateByAdminDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.exception.UsernameAlreadyInUseException;
import by.teachmeskills.devteam.service.UserService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("web")
@WebMvcTest(AdminController.class)
@Import(SecurityConfiguration.class)
class AdminControllerWebTest {

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
    @WithMockUser(authorities = "ADMIN")
    void shouldRenderAdminPanel_whenGetAdmin_givenUserRoleAdmin() throws Exception {
        // given
        var usersList = List.of(USER_DTO_1, USER_DTO_2, USER_DTO_3);
        when(userServiceMock.findAll()).thenReturn(usersList);
        // when / then
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel"))
                .andExpect(model().attribute("usersList", usersList));
        verify(userServiceMock).findAll();
    }

    @Test
    @WithMockUser(authorities = "USER")
    void shouldForbidAccess_whenGetAdmin_givenNotAdminUser() throws Exception {
        // given
        // when / then
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(userServiceMock);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldRenderAdminUserEditor_whenGetUserIdEdit_givenUserId() throws Exception {
        // given
        var userDto = UserDto.builder()
                .id(USER_ID_1)
                .roles(Set.of(Role.USER))
                .build();
        when(userServiceMock.findById(any())).thenReturn(userDto);
        // when / then
        mockMvc.perform(get("/admin/{userId}/edit", USER_ID_1))
                .andExpect(status().isOk())
                .andExpect(view().name("adminUserEditor"))
                .andExpect(model().attribute("user", userDto))
                .andExpect(model().attribute("roles", Role.values()));
        verify(userServiceMock).findById(USER_ID_1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUpdateUserData_whenPostUserIdEdit_givenUserIdAndUpdateDataInParams() throws Exception {
        // given
        var givenUsername = "username";
        var givenRoles = Set.of(Role.USER, Role.CUSTOMER);
        var expectedUserUpdateByAdminDto = UserUpdateByAdminDto.builder()
                .username(givenUsername)
                .roles(givenRoles)
                .build();
        // when / then
        mockMvc.perform(post("/admin/{userId}/edit", USER_ID_1)
                        .with(csrf())
                        .param("username", givenUsername)
                        .param("roles", Role.USER.name(), Role.CUSTOMER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
        verify(userServiceMock).updateUserByAdmin(USER_ID_1, expectedUserUpdateByAdminDto);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldRedirectToUserIdEditWithErrorMessage_whenPostUserIdEdit_givenUsernameAlreadyInUseExceptionThrownByService() throws Exception {
        // given
        var givenUsername = "username";
        var givenRoles = Set.of(Role.USER, Role.CUSTOMER);
        var expectedUserUpdateByAdminDto = UserUpdateByAdminDto.builder()
                .username(givenUsername)
                .roles(givenRoles)
                .build();
        final var usernameAlreadyInUseException = new UsernameAlreadyInUseException(givenUsername);
        doThrow(usernameAlreadyInUseException).when(userServiceMock).updateUserByAdmin(any(), any());
        // when / then
        mockMvc.perform(post("/admin/{userId}/edit", USER_ID_1)
                        .with(csrf())
                        .param("username", givenUsername)
                        .param("roles", Role.USER.name(), Role.CUSTOMER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/edit"))
                .andExpect(flash().attribute("errorMessage", usernameAlreadyInUseException.getMessage()));
        verify(userServiceMock).updateUserByAdmin(USER_ID_1, expectedUserUpdateByAdminDto);
    }
}