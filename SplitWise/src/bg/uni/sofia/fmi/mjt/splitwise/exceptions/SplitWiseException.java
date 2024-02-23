package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import java.io.Serializable;
import java.util.Arrays;

public class SplitWiseException implements Serializable {
    private Exception exception;
    private String user;
    private StackTraceElement[] stackTraceElements;

    public SplitWiseException(Exception exception, String user, StackTraceElement[] stackTraceElements) {
        this.exception = exception;
        this.user = user;
        this.stackTraceElements = stackTraceElements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(user).append("\n");
        sb.append("Exception: ").append(exception.toString()).append("\n");
        sb.append("StackTrace:\n");
        Arrays.stream(stackTraceElements).forEach(element -> sb.append(element).append("\n"));
        return sb.toString();
    }

}
