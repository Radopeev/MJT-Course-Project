package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class NoDueAmountFoundException extends Exception {
    public NoDueAmountFoundException(String message) {
        super(message);
    }

    public NoDueAmountFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
