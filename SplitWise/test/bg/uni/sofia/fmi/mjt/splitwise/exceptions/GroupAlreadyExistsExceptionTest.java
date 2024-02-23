package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GroupAlreadyExistsExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Group already exists exception message";
        GroupAlreadyExistsException groupAlreadyExistsException = new GroupAlreadyExistsException(errorMessage);

        assertEquals(errorMessage, groupAlreadyExistsException.getMessage(),"Error message should be" + errorMessage);
        assertNull(groupAlreadyExistsException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Group already exists exception message";
        Throwable cause = new RuntimeException("Cause of group already exists exception");
        GroupAlreadyExistsException groupAlreadyExistsException = new GroupAlreadyExistsException(errorMessage, cause);

        assertEquals(errorMessage, groupAlreadyExistsException.getMessage());
        assertEquals(cause, groupAlreadyExistsException.getCause());
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of group already exists exception");
        GroupAlreadyExistsException groupAlreadyExistsException = new GroupAlreadyExistsException("Group already exists exception", cause);

        assertEquals(cause, groupAlreadyExistsException.getCause());
    }
}

