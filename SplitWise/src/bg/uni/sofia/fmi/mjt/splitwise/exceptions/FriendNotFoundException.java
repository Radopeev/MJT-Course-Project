package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class FriendNotFoundException extends Exception {
    public FriendNotFoundException(String message) {
        super(message);
    }

    public FriendNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
