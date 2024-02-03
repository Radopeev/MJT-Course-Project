package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

public class InvalidCommand extends Exception{
    public InvalidCommand(String message){
        super(message);
    }
    public InvalidCommand(String message, Throwable cause){
        super(message,cause);
    }
}
