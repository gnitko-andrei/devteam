package by.teachmeskills.devteam.dto.user;

import by.teachmeskills.devteam.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import static by.teachmeskills.devteam.util.TextUtils.replaceHyphenationOnBr;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String contacts;
    private Integer price;
    private String skills;
    private Set<Role> roles;

    public String getRolesDescription() {
        var stringJoiner = new StringJoiner(", ");
        this.roles.stream().filter(role -> role != Role.USER).map(Role::getRoleName).forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    public String getFormattedUserInfo() {
        return Optional.ofNullable(firstName).orElse("") + " " + Optional.ofNullable(lastName).orElse("")
                + "\n" + Optional.ofNullable(lastName).orElse("")
                + "\n" + Optional.ofNullable(contacts).orElse("");
    }

    public String getFullName() {
        return Optional.ofNullable(firstName).orElse("")
                + Optional.ofNullable(lastName).orElse("");
    }

    public String getUserInfo() {
        String info = getFirstName() + " " + getLastName()
                + "\n" + getEmail()
                + "\n" + getContacts();
        return replaceHyphenationOnBr(info);
    }
}