package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class ExceptionSaverException extends RuntimeException{
    public ExceptionSaverException(String message) {
        super(message);
    }

    public ExceptionSaverException(String message, Throwable cause) {
        super(message, cause);
    }
}
