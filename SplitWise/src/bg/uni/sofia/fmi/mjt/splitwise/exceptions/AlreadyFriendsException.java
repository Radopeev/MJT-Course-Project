package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class AlreadyFriendsException extends Exception {
    public AlreadyFriendsException(String message) {
        super(message);
    }

    public AlreadyFriendsException(String message, Throwable cause) {
        super(message, cause);
    }
}
