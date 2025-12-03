package by.teachmeskills.devteam.entity.attributes.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TaskStatus {

    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String displayName;
}