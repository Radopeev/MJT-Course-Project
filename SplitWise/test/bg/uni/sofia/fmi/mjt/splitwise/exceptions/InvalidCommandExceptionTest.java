package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InvalidCommandExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Invalid command exception message";
        InvalidCommandException invalidCommandException = new InvalidCommandException(errorMessage);

        assertEquals(errorMessage, invalidCommandException.getMessage(),"Error message should be " + errorMessage);
        assertNull(invalidCommandException.getCause(),"Cause is not null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Invalid command exception message";
        Throwable cause = new RuntimeException("Cause of invalid command exception");
        InvalidCommandException invalidCommandException = new InvalidCommandException(errorMessage, cause);

        assertEquals(errorMessage, invalidCommandException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, invalidCommandException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of invalid command exception");
        InvalidCommandException invalidCommandException = new InvalidCommandException("Invalid command exception", cause);

        assertEquals(cause, invalidCommandException.getCause(),"Cause is not correct");
    }
}

