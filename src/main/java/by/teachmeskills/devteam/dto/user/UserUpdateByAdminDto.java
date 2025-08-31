package by.teachmeskills.devteam.dto.user;

import by.teachmeskills.devteam.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserUpdateByAdminDto {
    private String username;
    private Set<Role> roles;
}