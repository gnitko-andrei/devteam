package by.teachmeskills.devteam.exception;

public class UsernameAlreadyInUseException extends RuntimeException {
    public UsernameAlreadyInUseException(String username) {
        super("Cannot update user. Username '%s' is already in use".formatted(username));
    }
}