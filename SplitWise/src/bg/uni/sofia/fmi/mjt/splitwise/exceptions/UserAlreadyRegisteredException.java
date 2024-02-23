package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class UserAlreadyRegisteredException extends Exception {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public UserAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}
