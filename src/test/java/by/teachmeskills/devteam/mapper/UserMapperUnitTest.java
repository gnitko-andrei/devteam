package by.teachmeskills.devteam.mapper;

import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.dto.user.UserRegistrationDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperUnitTest {

    public static final String USERNAME_1 = "username_1";
    public static final String USERNAME_2 = "username_2";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final int PRICE = 100;
    public static final String CONTACTS = "contacts";
    public static final String SKILLS = "skills";
    public static final Set<Role> ROLES = Set.of(Role.USER, Role.MANAGER);
    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;

    @Test
    void shouldMapAllFields_whenToUserDto_givenUserEntityWithAllData() {
        // given
        var mapper = new UserMapper();
        var givenUserEntity = User.builder()
                .id(ID_1)
                .username(USERNAME_1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .price(PRICE)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .roles(ROLES)
                .build();
        var expectedUserDto = UserDto.builder()
                .id(ID_1)
                .username(USERNAME_1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .price(PRICE)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .roles(ROLES)
                .formattedUserInfo(givenUserEntity.getFormattedUserInfo())
                .fullName(givenUserEntity.getFullName())
                .rolesDescription(givenUserEntity.getRolesDescription())
                .build();
        // when
        var actual = mapper.toUserDto(givenUserEntity);
        // then
        assertThat(actual).isEqualTo(expectedUserDto);
    }

    @Test
    void shouldMapExistingFields_whenToUserDto_givenUserEntityWithMandatoryData() {
        // given
        var mapper = new UserMapper();
        var givenUserEntity = User.builder()
                .id(ID_1)
                .username(USERNAME_1)
                .roles(ROLES)
                .build();
        var expectedUserDto = UserDto.builder()
                .id(ID_1)
                .username(USERNAME_1)
                .roles(ROLES)
                .formattedUserInfo(givenUserEntity.getFormattedUserInfo())
                .fullName(givenUserEntity.getFullName())
                .rolesDescription(givenUserEntity.getRolesDescription())
                .build();
        // when
        var actual = mapper.toUserDto(givenUserEntity);
        // then
        assertThat(actual).isEqualTo(expectedUserDto);
    }

    @Test
    void shouldMapAllFields_whenToEntity_givenUserRegistrationDtoWithAllData() {
        // given
        var mapper = new UserMapper();
        var givenUserDto = UserRegistrationDto.builder()
                .username(USERNAME_1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .roles(ROLES)
                .build();
        var expectedUserEntity = User.builder()
                .username(USERNAME_1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .contacts(CONTACTS)
                .skills(SKILLS)
                .roles(ROLES)
                .build();
        // when
        var actual = mapper.toEntity(givenUserDto);
        // then
        assertThat(actual).isEqualTo(expectedUserEntity);
    }

    @Test
    void shouldMapExistingFields_whenToEntity_givenUserRegistrationDtoWithMandatoryData() {
        // given
        var mapper = new UserMapper();
        var givenUserDto = UserRegistrationDto.builder()
                .username(USERNAME_1)
                .roles(ROLES)
                .build();
        var expectedUserEntity = User.builder()
                .username(USERNAME_1)
                .roles(ROLES)
                .build();
        // when
        var actual = mapper.toEntity(givenUserDto);
        // then
        assertThat(actual).isEqualTo(expectedUserEntity);
    }

    @Test
    void shouldMapAllEntitiesToUserDto_whenToUserDtoList_givenUserEntitiesList() {
        // given
        var mapper = new UserMapper();
        var givenUserEntity1 = User.builder()
                .id(ID_1)
                .username(USERNAME_1)
                .roles(ROLES)
                .build();
        var givenUserEntity2 = User.builder()
                .id(ID_2)
                .username(USERNAME_2)
                .roles(ROLES)
                .build();
        var giverUserList = List.of(givenUserEntity1, givenUserEntity2);
        // when
        var actual = mapper.toUserDtoList(giverUserList);
        // then
        assertThat(actual).hasSize(2).satisfies(list -> {
            assertThat(list.get(0).getId()).isEqualTo(ID_1);
            assertThat(list.get(1).getId()).isEqualTo(ID_2);
        });
    }

    @Test
    void shouldReturnEmptyList_whenToUserDtoList_givenEmptyList() {
        // given
        var mapper = new UserMapper();
        var giverUserList = new ArrayList<User>();
        // when
        var actual = mapper.toUserDtoList(giverUserList);
        // then
        assertThat(actual).isEmpty();
    }
}