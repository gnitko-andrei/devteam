package by.teachmeskills.devteam.entity.attributes.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum ProjectStatus {

    NEW("New"),
    PENDING_MANAGER("Waiting for approval"),
    IN_PROGRESS("In progress"),
    COMPLETED("Done");

    private final String displayName;

    public static Set<ProjectStatus> visibleForFiltering() {
        return EnumSet.of(PENDING_MANAGER, IN_PROGRESS, COMPLETED);
    }
}