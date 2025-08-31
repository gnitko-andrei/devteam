package by.teachmeskills.devteam.entity.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum ProjectStatus {

    NEW("Новый"),
    PENDING_MANAGER("Oжидание обработки менеджером"),
    IN_PROGRESS("B разработке"),
    COMPLETED("Завершён");

    private final String displayName;

    public static Set<ProjectStatus> visibleForFiltering() {
        return EnumSet.of(PENDING_MANAGER, IN_PROGRESS, COMPLETED);
    }
}