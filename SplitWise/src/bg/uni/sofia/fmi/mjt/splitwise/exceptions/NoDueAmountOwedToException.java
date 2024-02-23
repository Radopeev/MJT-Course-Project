package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class NoDueAmountOwedToException extends Exception {
    public NoDueAmountOwedToException(String message) {
        super(message);
    }

    public NoDueAmountOwedToException(String message, Throwable cause) {
        super(message, cause);
    }
}
