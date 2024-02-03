package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class NotMemberOfGroup extends Exception{
    public NotMemberOfGroup(String message){
        super(message);
    }
    public NotMemberOfGroup(String message, Throwable e){
        super(message,e);
    }
}
