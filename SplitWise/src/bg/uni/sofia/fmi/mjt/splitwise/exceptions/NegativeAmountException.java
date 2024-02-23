package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class NegativeAmountException extends Exception {
    public NegativeAmountException(String message) {
        super(message);
    }

    public NegativeAmountException(String message, Throwable e) {
        super(message, e);
    }
}
