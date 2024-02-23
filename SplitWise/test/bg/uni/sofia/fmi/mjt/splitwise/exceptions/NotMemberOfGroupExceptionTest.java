package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NotMemberOfGroupExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Not a member of group exception message";
        NotMemberOfGroupException notMemberOfGroupException = new NotMemberOfGroupException(errorMessage);

        assertEquals(errorMessage, notMemberOfGroupException.getMessage(),"Error message should be " + errorMessage);
        assertNull(notMemberOfGroupException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Not a member of group exception message";
        Throwable cause = new RuntimeException("Cause of not a member of group exception");
        NotMemberOfGroupException notMemberOfGroupException = new NotMemberOfGroupException(errorMessage, cause);

        assertEquals(errorMessage, notMemberOfGroupException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, notMemberOfGroupException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of not a member of group exception");
        NotMemberOfGroupException notMemberOfGroupException = new NotMemberOfGroupException("Not a member of group exception", cause);

        assertEquals(cause, notMemberOfGroupException.getCause(),"Cause is not correct");
    }
}

