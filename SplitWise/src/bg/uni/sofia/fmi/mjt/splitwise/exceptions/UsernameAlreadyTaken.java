package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class UsernameAlreadyTaken extends Exception {
    public UsernameAlreadyTaken(String message){
        super(message);
    }

    public UsernameAlreadyTaken(String message, Throwable cause){
        super(message,cause);
    }
}
