package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import bg.uni.sofia.fmi.mjt.splitwise.group.Group;

public class GroupAlreadyExists extends Exception{
    public GroupAlreadyExists(String message){
        super(message);
    }
    public GroupAlreadyExists(String message, Throwable cause){
        super(message,cause);
    }
}
