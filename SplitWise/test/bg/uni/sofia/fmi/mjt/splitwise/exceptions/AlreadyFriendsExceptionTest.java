package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AlreadyFriendsExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Already friends exception message";
        AlreadyFriendsException alreadyFriendsException = new AlreadyFriendsException(errorMessage);

        assertEquals(errorMessage, alreadyFriendsException.getMessage(),"Error message should be" + errorMessage);
        assertNull(alreadyFriendsException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Already friends exception message";
        Throwable cause = new RuntimeException("Cause of already friends exception");
        AlreadyFriendsException alreadyFriendsException = new AlreadyFriendsException(errorMessage, cause);

        assertEquals(errorMessage, alreadyFriendsException.getMessage(),"Error message should be" + errorMessage);
        assertEquals(cause, alreadyFriendsException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of already friends exception");
        AlreadyFriendsException alreadyFriendsException = new AlreadyFriendsException("Already friends exception", cause);

        assertEquals(cause, alreadyFriendsException.getCause(), "Cause is not correct");
    }
}

