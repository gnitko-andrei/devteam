package by.teachmeskills.devteam.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("Project not found. Project Id: %s".formatted(projectId));
    }
}