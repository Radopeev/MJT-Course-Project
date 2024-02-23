package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class NotMemberOfGroupException extends Exception {
    public NotMemberOfGroupException(String message) {
        super(message);
    }

    public NotMemberOfGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
