package by.teachmeskills.devteam.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task not found. Task Id: %s".formatted(taskId));
    }
}