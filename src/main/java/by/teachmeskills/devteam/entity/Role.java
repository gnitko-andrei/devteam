package by.teachmeskills.devteam.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {

    USER("Пользователь"),
    ADMIN("Администратор"),
    CUSTOMER("Заказчик"),
    MANAGER("Менеджер"),
    DEVELOPER("Разработчик");

    final String roleName;

    @Override
    public String getAuthority() {
        return name();
    }
}
