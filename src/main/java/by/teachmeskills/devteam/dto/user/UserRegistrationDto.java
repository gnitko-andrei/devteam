package by.teachmeskills.devteam.dto.user;

import by.teachmeskills.devteam.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserRegistrationDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String contacts;
    private String skills;
    private Set<Role> roles;
}