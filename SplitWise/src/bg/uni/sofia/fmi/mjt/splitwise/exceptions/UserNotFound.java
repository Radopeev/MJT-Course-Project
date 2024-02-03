package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class UserNotFound extends Exception{
    public UserNotFound(String message){
        super(message);
    }

    public UserNotFound(String message, Throwable cause){
        super(message,cause);
    }
}
