package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserAlreadyRegisteredExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "User already registered exception message";
        UserAlreadyRegisteredException userAlreadyRegisteredException = new UserAlreadyRegisteredException(errorMessage);

        assertEquals(errorMessage, userAlreadyRegisteredException.getMessage(),"Error message should be " + errorMessage);
        assertNull(userAlreadyRegisteredException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "User already registered exception message";
        Throwable cause = new RuntimeException("Cause of user already registered exception");
        UserAlreadyRegisteredException userAlreadyRegisteredException = new UserAlreadyRegisteredException(errorMessage, cause);

        assertEquals(errorMessage, userAlreadyRegisteredException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, userAlreadyRegisteredException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of user already registered exception");
        UserAlreadyRegisteredException userAlreadyRegisteredException = new UserAlreadyRegisteredException("User already registered exception", cause);

        assertEquals(cause, userAlreadyRegisteredException.getCause(),"Cause is not correct");
    }
}
