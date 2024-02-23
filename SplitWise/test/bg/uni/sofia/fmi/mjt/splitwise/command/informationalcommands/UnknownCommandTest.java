package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnknownCommandTest {

    @Test
    public void testExecute() {
        Command unknownCommand = new UnknownCommand();
        String expectedOutput = "Unknown command! Please try again or type help for help" + System.lineSeparator();

        assertEquals(expectedOutput, unknownCommand.execute(), "Result should be" + expectedOutput);
    }
}
