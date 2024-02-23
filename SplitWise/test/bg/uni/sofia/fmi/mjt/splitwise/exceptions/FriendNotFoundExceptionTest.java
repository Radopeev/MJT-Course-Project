package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FriendNotFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Friend not found exception message";
        FriendNotFoundException friendNotFoundException = new FriendNotFoundException(errorMessage);

        assertEquals(errorMessage, friendNotFoundException.getMessage(),"Error message should be" + errorMessage);
        assertNull(friendNotFoundException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Friend not found exception message";
        Throwable cause = new RuntimeException("Cause of friend not found exception");
        FriendNotFoundException friendNotFoundException = new FriendNotFoundException(errorMessage, cause);

        assertEquals(errorMessage, friendNotFoundException.getMessage(),"Error message should be" + errorMessage);
        assertEquals(cause, friendNotFoundException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of friend not found exception");
        FriendNotFoundException friendNotFoundException = new FriendNotFoundException("Friend not found exception", cause);

        assertEquals(cause, friendNotFoundException.getCause(),"Cause is not correct");
    }
}

