package by.teachmeskills.devteam.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User not found. Id: %s".formatted(userId));
    }
}