package by.teachmeskills.devteam.mapper;

import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.dto.user.UserRegistrationDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .price(user.getPrice())
                .contacts(user.getContacts())
                .skills(user.getSkills())
                .roles(user.getRoles())
                .formattedUserInfo(user.getFormattedUserInfo())
                .rolesDescription(user.getRolesDescription())
                .build();
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(this::toUserDto).toList();
    }

    public User toEntity(UserRegistrationDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .contacts(dto.getContacts())
                .skills(dto.getSkills())
                .roles(Set.of(Role.USER, dto.getUserRole()))
                .build();
    }
}