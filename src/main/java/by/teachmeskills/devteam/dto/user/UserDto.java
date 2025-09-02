package by.teachmeskills.devteam.dto.user;

import by.teachmeskills.devteam.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String contacts;
    private Integer price;
    private String skills;
    private Set<Role> roles;
    private String formattedUserInfo;
    private String rolesDescription;
}