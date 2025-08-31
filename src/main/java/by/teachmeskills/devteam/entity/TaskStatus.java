package by.teachmeskills.devteam.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TaskStatus {

    NEW("Новая"),
    IN_PROGRESS("B разработке"),
    DONE("Завершена");

    private final String displayName;
}