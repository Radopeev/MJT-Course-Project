package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class GroupNotFound extends Exception{
    public GroupNotFound(String message){
        super(message);
    }
    public GroupNotFound(String message, Throwable e){
        super(message,e);
    }
}
