package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class AlreadyFriends extends Exception{
    public AlreadyFriends(String message){
        super(message);
    }
    public AlreadyFriends(String message, Throwable cause){
        super(message,cause);
    }
}
