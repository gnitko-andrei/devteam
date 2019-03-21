package by.teachmeskills.devteam.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, CUSTOMER, MANAGER, DEVELOPER;

    @Override
    public String getAuthority() {
        return name();
    }
}
