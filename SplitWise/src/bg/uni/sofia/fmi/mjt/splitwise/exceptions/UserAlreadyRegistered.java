package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class UserAlreadyRegistered extends Exception{

    public UserAlreadyRegistered(String message){
        super(message);
    }

    public UserAlreadyRegistered(String message, Throwable cause){
        super(message,cause);
    }
}
