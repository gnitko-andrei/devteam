package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.dto.user.UserProfileUpdateDto;
import by.teachmeskills.devteam.dto.user.UserRegistrationDto;
import by.teachmeskills.devteam.dto.user.UserUpdateByAdminDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.exception.UserNotFoundException;
import by.teachmeskills.devteam.exception.UsernameAlreadyInUseException;
import by.teachmeskills.devteam.exception.WrongPasswordException;
import by.teachmeskills.devteam.mapper.UserMapper;
import by.teachmeskills.devteam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    public static final String USERNAME = "username";
    public static final String NEW_USERNAME = "new_username";
    public static final String ENCODED_PASSWORD_1 = "$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG";
    public static final String ENCODED_PASSWORD_2 = "$1a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG";
    public static final String PASSWORD_1 = "1";
    public static final String PASSWORD_2 = "2";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String CONTACTS = "contacts";
    public static final String SKILLS = "skills";
    public static final long ID_1 = 1L;

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private UserMapper userMapperMock;
    @Mock
    private User userMock;
    @Mock
    private User userMock1;
    @Mock
    private User userMock2;
    @Mock
    private UserDto userDtoMock;
    @Mock
    private UserDto userDtoMock1;
    @Mock
    private UserDto userDtoMock2;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserDetails_whenLoadUserByUsername_givenUsernameOfExistingUser() {
        // given
        when(userRepositoryMock.findByUsername(any())).thenReturn(Optional.of(userMock));
        // when
        var actual = userService.loadUserByUsername(USERNAME);
        // then
        verify(userRepositoryMock).findByUsername(USERNAME);
        assertThat(actual).isEqualTo(userMock);
    }

    @Test
    void shouldThrowUsernameNotFoundException_whenLoadUserByUsername_givenUnknownUsername() {
        // given
        when(userRepositoryMock.findByUsername(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.loadUserByUsername(USERNAME))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found. Username: " + USERNAME);
        verify(userRepositoryMock).findByUsername(USERNAME);
    }

    @Test
    void shouldReturnUserDto_whenFindById_givenIdOfExistingUser() {
        // given
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userMapperMock.toUserDto(any())).thenReturn(userDtoMock);
        // when
        var actual = userService.findById(ID_1);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userMapperMock).toUserDto(userMock);
        assertThat(actual).isEqualTo(userDtoMock);
    }

    @Test
    void shouldThrowUserNotFoundException_whenFindById_givenUnknownUserId() {
        // given
        when(userRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.findById(ID_1))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + ID_1);
        verify(userRepositoryMock).findById(ID_1);
        verifyNoInteractions(userMapperMock);
    }

    @Test
    void shouldReturnListOfUserDto_whenFindAll_givenUsersExists() {
        // given
        when(userRepositoryMock.findAll()).thenReturn(List.of(userMock, userMock1, userMock2));
        when(userMapperMock.toUserDto(any())).thenReturn(userDtoMock, userDtoMock1, userDtoMock2);
        // when
        var actual = userService.findAll();
        // then
        verify(userRepositoryMock).findAll();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapperMock, times(3)).toUserDto(userCaptor.capture());
        assertThat(userCaptor.getAllValues()).containsExactly(userMock, userMock1, userMock2);
        assertThat(actual).containsExactly(userDtoMock, userDtoMock1, userDtoMock2);
    }

    @Test
    void shouldReturnEmptyList_whenFindAll_givenNoUsersExists() {
        // given
        when(userRepositoryMock.findAll()).thenReturn(List.of());
        // when
        var actual = userService.findAll();
        // then
        verify(userRepositoryMock).findAll();
        verifyNoInteractions(userMapperMock);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnListOfUserDto_whenGetAllUsersByRole_givenUsersWithGivenRoleExists() {
        // given
        when(userRepositoryMock.findAllByRolesContainsOrderByUsernameAsc(any())).thenReturn(List.of(userMock, userMock1, userMock2));
        when(userMapperMock.toUserDto(any())).thenReturn(userDtoMock, userDtoMock1, userDtoMock2);
        // when
        var actual = userService.getAllUsersByRole(Role.MANAGER);
        // then
        verify(userRepositoryMock).findAllByRolesContainsOrderByUsernameAsc(Role.MANAGER);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapperMock, times(3)).toUserDto(userCaptor.capture());
        assertThat(userCaptor.getAllValues()).containsExactly(userMock, userMock1, userMock2);
        assertThat(actual).containsExactly(userDtoMock, userDtoMock1, userDtoMock2);
    }

    @Test
    void shouldReturnEmptyList_whenGetAllUsersByRole_givenUsersWithGivenRoleExists() {
        // given
        when(userRepositoryMock.findAllByRolesContainsOrderByUsernameAsc(any())).thenReturn(List.of());
        // when
        var actual = userService.getAllUsersByRole(Role.MANAGER);
        // then
        verify(userRepositoryMock).findAllByRolesContainsOrderByUsernameAsc(Role.MANAGER);
        verifyNoInteractions(userMapperMock);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnTrue_whenIsUserExists_givenExistingUsername() {
        // given
        when(userRepositoryMock.findByUsername(any())).thenReturn(Optional.of(userMock));
        // when
        var actual = userService.isUserExists(USERNAME);
        // then
        verify(userRepositoryMock).findByUsername(USERNAME);
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnFalse_whenIsUserExists_givenNotExistingUsername() {
        // given
        when(userRepositoryMock.findByUsername(any())).thenReturn(Optional.empty());
        // when
        var actual = userService.isUserExists(USERNAME);
        // then
        verify(userRepositoryMock).findByUsername(USERNAME);
        assertThat(actual).isFalse();
    }

    @Test
    void shouldPrepareUserEntityAndSaveItToDb_whenCreateNewUser_givenUserRegistrationDtoAndUserRole() {
        // given
        var givenUserRegistrationDto = UserRegistrationDto.builder()
                .username(USERNAME)
                .password(PASSWORD_1)
                .userRole(Role.DEVELOPER)
                .build();
        when(userMapperMock.toEntity(any())).thenReturn(userMock);
        when(passwordEncoderMock.encode(any())).thenReturn(ENCODED_PASSWORD_1);
        // when
        userService.createNewUser(givenUserRegistrationDto);
        // then
        verify(userMapperMock).toEntity(givenUserRegistrationDto);
        verify(userMock).setActive(true);
        verify(passwordEncoderMock).encode(givenUserRegistrationDto.getPassword());
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldUpdateUserRoles_whenUpdateUserByAdmin_givenUserUpdateDtoAndUsernameIsBlank() {
        // given
        var givenUserUpdateDto = UserUpdateByAdminDto.builder()
                .roles(Set.of(Role.USER, Role.MANAGER))
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userMock.getUsername()).thenReturn(USERNAME);
        // when
        userService.updateUserByAdmin(ID_1, givenUserUpdateDto);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userMock, never()).setUsername(any());
        verify(userMock).setRoles(givenUserUpdateDto.getRoles());
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldUpdateUserRoles_whenUpdateUserByAdmin_givenUserUpdateDtoAndUsernameNotUpdated() {
        // given
        var givenUserUpdateDto = UserUpdateByAdminDto.builder()
                .username(USERNAME)
                .roles(Set.of(Role.USER, Role.MANAGER))
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userMock.getUsername()).thenReturn(USERNAME);
        // when
        userService.updateUserByAdmin(ID_1, givenUserUpdateDto);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userMock, never()).setUsername(any());
        verify(userMock).setRoles(givenUserUpdateDto.getRoles());
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldUpdateUserRolesAndUsername_whenUpdateUserByAdmin_givenUserUpdateDtoAndUsernameUpdated() {
        // given
        var givenUserUpdateDto = UserUpdateByAdminDto.builder()
                .username(NEW_USERNAME)
                .roles(Set.of(Role.USER, Role.MANAGER))
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userRepositoryMock.findByUsername(any())).thenReturn(Optional.empty());
        when(userMock.getUsername()).thenReturn(USERNAME);
        // when
        userService.updateUserByAdmin(ID_1, givenUserUpdateDto);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userRepositoryMock).findByUsername(NEW_USERNAME);
        verify(userMock).setUsername(NEW_USERNAME);
        verify(userMock).setRoles(givenUserUpdateDto.getRoles());
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldThrowUsernameAlreadyInUseException_whenUpdateUserByAdmin_givenUserUpdateDtoAndUsernameUpdatedButAlreadyInUseByAnotherUser() {
        // given
        var givenUserUpdateDto = UserUpdateByAdminDto.builder()
                .username(NEW_USERNAME)
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userRepositoryMock.findByUsername(any())).thenReturn(Optional.of(userMock1));
        when(userMock.getUsername()).thenReturn(USERNAME);
        // when / then
        assertThatThrownBy(() -> userService.updateUserByAdmin(ID_1, givenUserUpdateDto))
                .isInstanceOf(UsernameAlreadyInUseException.class)
                .hasMessage("Cannot update user. Username 'new_username' is already in use");
        verify(userRepositoryMock).findById(ID_1);
        verify(userRepositoryMock).findByUsername(NEW_USERNAME);
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    void shouldThrowUserNotFoundException_whenUpdateUserByAdmin_givenUserUpdateDtoAndNotExistingUserId() {
        // given
        var givenUserUpdateDto = UserUpdateByAdminDto.builder()
                .username(NEW_USERNAME)
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.updateUserByAdmin(ID_1, givenUserUpdateDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + ID_1);
        verify(userRepositoryMock).findById(ID_1);
    }

    @Test
    void shouldUpdateUserDataAndPassword_whenUpdateUserProfile_givenUserProfileUpdateDtoWithCorrectCurrentPasswordAndNewPassword() {
        // given
        var givenUserUpdateDto = UserProfileUpdateDto.builder()
                .currentPassword(PASSWORD_1)
                .newPassword(PASSWORD_2)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userMock.getPassword()).thenReturn(PASSWORD_1);
        when(passwordEncoderMock.matches(any(), any())).thenReturn(true);
        when(passwordEncoderMock.encode(any())).thenReturn(ENCODED_PASSWORD_2);
        // when
        userService.updateUserProfile(ID_1, givenUserUpdateDto);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(passwordEncoderMock).matches(givenUserUpdateDto.getCurrentPassword(), userMock.getPassword());
        verify(passwordEncoderMock).encode(PASSWORD_2);
        verify(userMock).setPassword(ENCODED_PASSWORD_2);
        verify(userMock).setFirstName(FIRST_NAME);
        verify(userMock).setLastName(LAST_NAME);
        verify(userMock).setEmail(EMAIL);
        verify(userMock).setContacts(CONTACTS);
        verify(userMock).setSkills(SKILLS);
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldUpdateUserDataWithoutPassword_whenUpdateUserProfile_givenUserProfileUpdateDtoWithBlankNewPasswordAndWithoutCurrentPassword() {
        // given
        var givenUserUpdateDto = UserProfileUpdateDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        // when
        userService.updateUserProfile(ID_1, givenUserUpdateDto);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userMock, never()).setPassword(any());
        verify(userMock).setFirstName(FIRST_NAME);
        verify(userMock).setLastName(LAST_NAME);
        verify(userMock).setEmail(EMAIL);
        verify(userMock).setContacts(CONTACTS);
        verify(userMock).setSkills(SKILLS);
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldThrowWrongPasswordException_whenUpdateUserProfile_givenUserProfileUpdateDtoWithWrongCurrentPasswordAndNotBlankNewPassword() {
        // given
        var givenUserUpdateDto = UserProfileUpdateDto.builder()
                .currentPassword(PASSWORD_1)
                .newPassword("newPassword")
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        when(userMock.getPassword()).thenReturn(PASSWORD_1);
        when(passwordEncoderMock.matches(any(), any())).thenReturn(false);
        // when / then
        assertThatThrownBy(() -> userService.updateUserProfile(ID_1, givenUserUpdateDto))
                .isInstanceOf(WrongPasswordException.class)
                .hasMessage("Wrong current password provided!");
        verify(userRepositoryMock).findById(ID_1);
        verify(passwordEncoderMock).matches(givenUserUpdateDto.getCurrentPassword(), userMock.getPassword());
    }

    @Test
    void shouldThrowWrongPasswordException_whenUpdateUserProfile_givenUserProfileUpdateDtoWithBlankCurrentPasswordAndNotBlankNewPassword() {
        // given
        var givenUserUpdateDto = UserProfileUpdateDto.builder()
                .newPassword("newPassword")
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        // when / then
        assertThatThrownBy(() -> userService.updateUserProfile(ID_1, givenUserUpdateDto))
                .isInstanceOf(WrongPasswordException.class)
                .hasMessage("Wrong current password provided!");
        verify(userRepositoryMock).findById(ID_1);
    }

    @Test
    void shouldThrowUserNotFoundException_whenUpdateUserProfile_givenUserProfileUpdateDtoAndNotExistingUserId() {
        // given
        var givenUserUpdateDto = UserProfileUpdateDto.builder()
                .build();
        when(userRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.updateUserProfile(ID_1, givenUserUpdateDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + ID_1);
        verify(userRepositoryMock).findById(ID_1);
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    void shouldUpdateUserPrice_whenUpdateDeveloperRate_givenUserIdAndNewPrice() {
        // given
        var givenPrice = 100;
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        // when
        userService.updateDeveloperRate(ID_1, givenPrice);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userMock).setPrice(givenPrice);
        verify(userRepositoryMock).save(userMock);
    }

    @Test
    void shouldThrowUserNotFoundException_whenUpdateDeveloperRate_givenUnknownUserId() {
        // given
        when(userRepositoryMock.findById(ID_1)).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.updateDeveloperRate(ID_1, 100))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + ID_1);
        verify(userRepositoryMock).findById(ID_1);
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    void shouldDeleteUser_whenDeleteById_givenUserId() {
        // given
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        // when
        userService.deleteById(ID_1);
        // then
        verify(userRepositoryMock).findById(ID_1);
        verify(userRepositoryMock).delete(userMock);
    }

    @Test
    void shouldThrowUserNotFoundException_whenDeleteById_givenUnknownUserId() {
        // given
        when(userRepositoryMock.findById(ID_1)).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.deleteById(ID_1))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + ID_1);
        verify(userRepositoryMock).findById(ID_1);
        verify(userRepositoryMock, never()).delete(any());
    }
}