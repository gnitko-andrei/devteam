package by.teachmeskills.devteam.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {

    USER("User"),
    ADMIN("Administrator"),
    CUSTOMER("Customer"),
    MANAGER("Manager"),
    DEVELOPER("Developer");

    final String roleName;

    @Override
    public String getAuthority() {
        return name();
    }
}
