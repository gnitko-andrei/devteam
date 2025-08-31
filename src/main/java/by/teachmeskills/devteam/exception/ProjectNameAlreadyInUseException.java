package by.teachmeskills.devteam.exception;

public class ProjectNameAlreadyInUseException extends RuntimeException {
    public ProjectNameAlreadyInUseException(String username) {
        super("Cannot create/update the project. Project name '%s' is already in use".formatted(username));
    }
}