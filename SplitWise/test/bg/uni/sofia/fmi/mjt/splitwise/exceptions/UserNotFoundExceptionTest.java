package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserNotFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "User not found exception message";
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage);

        assertEquals(errorMessage, userNotFoundException.getMessage(),"Error message should be " + errorMessage);
        assertNull(userNotFoundException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "User not found exception message";
        Throwable cause = new RuntimeException("Cause of user not found exception");
        UserNotFoundException userNotFoundException = new UserNotFoundException(errorMessage, cause);

        assertEquals(errorMessage, userNotFoundException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, userNotFoundException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of user not found exception");
        UserNotFoundException userNotFoundException = new UserNotFoundException("User not found exception", cause);

        assertEquals(cause, userNotFoundException.getCause(),"Cause is not correct");
    }
}

