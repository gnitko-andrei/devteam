package by.teachmeskills.devteam.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserUnitTest {
    @Test
    void shouldReturnFullName_whenGetFullName_givenUserWithFirstAndLstName() {
        // given
        var givenFirstName = "firstName";
        var givenLastName = "lastName";
        var givenUser = User.builder()
                .firstName(givenFirstName)
                .lastName(givenLastName)
                .build();
        var expectedFullName = "firstName lastName";
        // when
        var actual = givenUser.getFullName();
        // then
        assertThat(actual).isEqualTo(expectedFullName);
    }

    @Test
    void shouldReturnUsername_whenGetFullName_givenUserWithoutFirstAndLstName() {
        // given
        var givenUsername = "username";
        var givenUser = User.builder()
                .username(givenUsername)
                .build();
        // when
        var actual = givenUser.getFullName();
        // then
        assertThat(actual).isEqualTo(givenUsername);
    }

    @Test
    void shouldReturnFormattedUserInfo_whenGetFormattedUserInfo_givenUserWithAllUserInfo() {
        // given
        var givenFirstName = "firstName";
        var givenLastName = "lastName";
        var givenEmail = "email@gmail.com";
        var givenContacts = "contacts";
        var givenUser = User.builder()
                .firstName(givenFirstName)
                .lastName(givenLastName)
                .email(givenEmail)
                .contacts(givenContacts)
                .build();
        var expectedUserInfo = """
                firstName lastName
                email@gmail.com
                contacts""";
        // when
        var actual = givenUser.getFormattedUserInfo();
        // then
        assertThat(actual).isEqualTo(expectedUserInfo);
    }

    @Test
    void shouldReturnUsername_whenGetFormattedUserInfo_givenUserWithoutAnyUserInfoField() {
        // given
        var givenUsername = "username";
        var givenUser = User.builder()
                .username(givenUsername)
                .build();
        // when
        var actual = givenUser.getFormattedUserInfo();
        // then
        assertThat(actual).isEqualTo(givenUsername);
    }

    @Test
    void shouldReturnRolesDescriptionString_whenGetRolesDescription_givenUserWithMultipleRoles() {
        // given
        var givenUser = User.builder()
                .roles(Set.of(Role.USER, Role.MANAGER, Role.ADMIN))
                .build();
        var expectedRolesDescription = "Администратор, Менеджер";
        // when
        var actual = givenUser.getRolesDescription();
        // then
        assertThat(actual).isEqualTo(expectedRolesDescription);
    }

    @Test
    void shouldReturnEmptyString_whenGetRolesDescription_givenUserWithOnlyUserBaseRole() {
        // given
        var givenUser = User.builder()
                .roles(Set.of(Role.USER))
                .build();
        // when
        var actual = givenUser.getRolesDescription();
        // then
        assertThat(actual).isBlank();
    }



}