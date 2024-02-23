package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class NoDueAmountFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "No due amount found exception message";
        NoDueAmountFoundException noDueAmountFoundException = new NoDueAmountFoundException(errorMessage);

        assertEquals(errorMessage, noDueAmountFoundException.getMessage(),"Error message should be " + errorMessage);
        assertNull(noDueAmountFoundException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "No due amount found exception message";
        Throwable cause = new RuntimeException("Cause of no due amount found exception");
        NoDueAmountFoundException noDueAmountFoundException = new NoDueAmountFoundException(errorMessage, cause);

        assertEquals(errorMessage, noDueAmountFoundException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, noDueAmountFoundException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of no due amount found exception");
        NoDueAmountFoundException noDueAmountFoundException = new NoDueAmountFoundException("No due amount found exception", cause);

        assertEquals(cause, noDueAmountFoundException.getCause(),"Cause is not correct");
    }
}
