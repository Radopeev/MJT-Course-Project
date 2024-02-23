package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class GroupAlreadyExistsException extends Exception {
    public GroupAlreadyExistsException(String message) {
        super(message);
    }

    public GroupAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
