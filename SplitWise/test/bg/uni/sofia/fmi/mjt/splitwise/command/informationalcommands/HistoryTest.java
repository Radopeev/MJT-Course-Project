package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.SplitHistoryEntry;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class HistoryTest {
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        user = mock(User.class);
    }

    @Test
    public void testExecuteWithEmptyHistory() {
        when(userRepository.getPaymentHistory(user)).thenReturn(new ArrayList<>());

        Command historyCommand = new History(userRepository, user);
        String result = historyCommand.execute();

        assertEquals("", result.trim(), "Result should be empty");
    }

    @Test
    public void testExecuteWithNonEmptyHistory() {
        List<SplitHistoryEntry> historyEntries = new ArrayList<>();
        historyEntries.add(new SplitHistoryEntry("Friend1", "Dinner", 20.0, false));
        historyEntries.add(new SplitHistoryEntry("Group1", "Movie", 30.0, true));

        when(userRepository.getPaymentHistory(user)).thenReturn(historyEntries);

        History historyCommand = new History(userRepository, user);
        String result = historyCommand.execute();

        String expected = "Friend Split - Friend1: Dinner, Amount: 20.00" +
            System.lineSeparator() +
            "Group Split - Group1: Movie, Amount: 30.00";

        assertEquals(expected, result.trim(),"Result should be" + expected);
    }
}
