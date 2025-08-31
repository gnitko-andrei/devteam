package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.exception.UserNotFoundException;
import by.teachmeskills.devteam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    public static final String ENCODED_PASSWORD = "encodedPassword";
    @Mock
    private User userMock;
    @Mock
    private UserRepository repositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    UserService userService;

    @Test
    void shouldReturnUserDetailsFromRepository_whenLoadUserByUsername_givenUserName() {
        // given
        var givenUserName = "username";
        when(repositoryMock.findByUsername(any())).thenReturn(Optional.of(userMock));
        // when
        var actual = userService.loadUserByUsername(givenUserName);
        // then
        assertThat(actual).isEqualTo(userMock);
        verify(repositoryMock).findByUsername(givenUserName);
    }

    @Test
    void shouldThrowException_whenFindByUsername_givenUserNameOfNonExistingUser() {
        // given
        var givenUserName = "username";
        when(repositoryMock.findByUsername(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.loadUserByUsername(givenUserName))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found. Username: username");
        verify(repositoryMock).findByUsername(givenUserName);
    }

    @Test
    void shouldReturnUserFromRepository_whenFindById_givenUserIdOfExistingUser() {
        // given
        var givenUserId = 1L;
        when(repositoryMock.findById(any())).thenReturn(Optional.of(userMock));
        // when
        var actual = userService.findById(givenUserId);
        // then
        assertThat(actual).isEqualTo(userMock);
        verify(repositoryMock).findById(givenUserId);
    }

    @Test
    void shouldThrowException_whenFindById_givenUserIdOfNonExistingUser() {
        // given
        var givenUserId = 1L;
        when(repositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> userService.findById(givenUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: 1");
        verify(repositoryMock).findById(givenUserId);
    }

    @Test
    void shouldReturnAllUsersFromRepository_whenFindAll_givenUserRepository() {
        // given
        var expectedUserList = asList(userMock, userMock, userMock);
        when(repositoryMock.findAll()).thenReturn(expectedUserList);
        // when
        var actual = userService.findAll();
        // then
        assertThat(actual).isEqualTo(expectedUserList);
        verify(repositoryMock).findAll();
    }


//    @Test
//    void shouldReturnAllUsersFromRepository_whenSaveNewUser_givenUserAndRoleName() {
//        // given
//        when(passwordEncoderMock.encode(any())).thenReturn(ENCODED_PASSWORD);
//        var expectedRoles = Set.of(Role.USER, Role.DEVELOPER);
//        // when
//        userService.saveNewUser(userMock, Role.DEVELOPER);
//        // then
//        verify(repositoryMock).save(userMock);
//        verify(userMock).setActive(true);
//        verify(passwordEncoderMock).encode(userMock.getPassword());
//        verify(userMock).setPassword(ENCODED_PASSWORD);
//        verify(userMock).setRoles(expectedRoles);
//    }



}