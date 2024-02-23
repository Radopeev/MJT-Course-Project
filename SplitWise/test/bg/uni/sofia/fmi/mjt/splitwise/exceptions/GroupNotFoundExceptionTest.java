package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GroupNotFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Group not found exception message";
        GroupNotFoundException groupNotFoundException = new GroupNotFoundException(errorMessage);

        assertEquals(errorMessage, groupNotFoundException.getMessage(),"Error message should be" + errorMessage);
        assertNull(groupNotFoundException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Group not found exception message";
        Throwable cause = new RuntimeException("Cause of group not found exception");
        GroupNotFoundException groupNotFoundException = new GroupNotFoundException(errorMessage, cause);

        assertEquals(errorMessage, groupNotFoundException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, groupNotFoundException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of group not found exception");
        GroupNotFoundException groupNotFoundException = new GroupNotFoundException("Group not found exception", cause);

        assertEquals(cause, groupNotFoundException.getCause(),"Cause is not correct");
    }
}
