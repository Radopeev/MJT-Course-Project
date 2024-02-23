package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class NoDueAmountOwedByException extends Exception{
    public NoDueAmountOwedByException(String message) {
        super(message);
    }

    public NoDueAmountOwedByException(String message, Throwable cause) {
        super(message, cause);
    }
}
