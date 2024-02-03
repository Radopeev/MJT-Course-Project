package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class FriendNotFound extends Exception{
    public FriendNotFound(String message){
        super(message);
    }
    public FriendNotFound(String message, Throwable cause){
        super(message,cause);
    }
}
