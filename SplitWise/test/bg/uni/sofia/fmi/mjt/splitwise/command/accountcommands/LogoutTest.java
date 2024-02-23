package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogoutTest {
    @Test
    void testExecute(){
        String result = new Logout().execute();
        String expected = "You have successfully logged out" + System.lineSeparator();

        assertEquals(expected,result, "Result should be " + expected);
    }
}
