package by.teachmeskills.devteam.entity;

import org.junit.jupiter.api.Test;

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

}