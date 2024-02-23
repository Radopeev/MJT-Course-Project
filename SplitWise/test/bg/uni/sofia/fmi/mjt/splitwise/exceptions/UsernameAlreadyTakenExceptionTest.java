package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UsernameAlreadyTakenExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Username already taken exception message";
        UsernameAlreadyTakenException usernameAlreadyTakenException = new UsernameAlreadyTakenException(errorMessage);

        assertEquals(errorMessage, usernameAlreadyTakenException.getMessage(),"Error message should be " + errorMessage);
        assertNull(usernameAlreadyTakenException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Username already taken exception message";
        Throwable cause = new RuntimeException("Cause of username already taken exception");
        UsernameAlreadyTakenException usernameAlreadyTakenException = new UsernameAlreadyTakenException(errorMessage, cause);

        assertEquals(errorMessage, usernameAlreadyTakenException.getMessage(),"Error message should be " +errorMessage);
        assertEquals(cause, usernameAlreadyTakenException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of username already taken exception");
        UsernameAlreadyTakenException usernameAlreadyTakenException = new UsernameAlreadyTakenException("Username already taken exception", cause);

        assertEquals(cause, usernameAlreadyTakenException.getCause(),"Cause is not correct");
    }
}
