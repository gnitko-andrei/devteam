package by.teachmeskills.devteam.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileUpdateDto {
    private String currentPassword;
    private String newPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String contacts;
    private String skills;
}