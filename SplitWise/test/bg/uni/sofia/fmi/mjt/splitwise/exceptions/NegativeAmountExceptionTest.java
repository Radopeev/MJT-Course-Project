package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NegativeAmountExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Negative amount exception message";
        NegativeAmountException negativeAmountException = new NegativeAmountException(errorMessage);

        assertEquals(errorMessage, negativeAmountException.getMessage(),"Error message should be " + errorMessage);
        assertNull(negativeAmountException.getCause(),"Cause should be null");
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String errorMessage = "Negative amount exception message";
        Throwable cause = new RuntimeException("Cause of negative amount exception");
        NegativeAmountException negativeAmountException = new NegativeAmountException(errorMessage, cause);

        assertEquals(errorMessage, negativeAmountException.getMessage(),"Error message should be " + errorMessage);
        assertEquals(cause, negativeAmountException.getCause(),"Cause is not correct");
    }

    @Test
    public void testGetCause() {
        Throwable cause = new RuntimeException("Cause of negative amount exception");
        NegativeAmountException negativeAmountException = new NegativeAmountException("Negative amount exception", cause);

        assertEquals(cause, negativeAmountException.getCause(),"Cause is not correct");
    }
}
