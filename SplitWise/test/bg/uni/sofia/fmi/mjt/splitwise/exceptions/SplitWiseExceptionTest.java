package bg.uni.sofia.fmi.mjt.splitwise.exceptions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SplitWiseExceptionTest {

    @Test
    void testToString() {
        Exception innerException = new UserNotFoundException("User with this username does not exists");
        String user = "testUser";
        StackTraceElement[] stackTrace = innerException.getStackTrace();
        
        SplitWiseException splitWiseException = new SplitWiseException(innerException, user, stackTrace);

        String expected = "User: testUser" + System.lineSeparator()
           + "Exception: bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException: User with this username does not exists" + System.lineSeparator()
           + "StackTrace:" + System.lineSeparator()
           + Arrays.toString(stackTrace).replace(", ", System.lineSeparator()).replace("[", "").replace("]", "") + System.lineSeparator();
        expected = expected.replaceAll("\\r\\n", "\n");
        String result = splitWiseException.toString().replaceAll("\\r\\n", "\n");

        assertEquals(expected, result,"Result should be " + expected);
    }
}

