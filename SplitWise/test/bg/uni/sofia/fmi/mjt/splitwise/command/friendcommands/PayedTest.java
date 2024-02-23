package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayedTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException, UsernameAlreadyTakenException, UserNotFoundException, AlreadyFriendsException {
        userRepository = new UserRepository(userFile,groupsFile);
        userRepository.addUser("rado", "rado");
        userRepository.addUser("dobo", "dobo");
        userRepository.addUser("kiro", "kiro");
        userRepository.addFriendToUser(userRepository.getUser("rado"), "kiro");
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecuteWhenFriendIsNotFound() {
        String username1 = "rado";
        String username2 = "dobo";
        double amount = 5;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String result = new Payed(userRepository, rado, username2, amount, reason).execute();
        String expected = "You are not friends with dobo" + System.lineSeparator();

        assertEquals(expected, result, "Result should be" + expected);
    }

    @Test
    void testExecuteWhenAmountIsNegative() {
        String username1 = "rado";
        String username2 = "kiro";
        double amount = -5;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String result = new Payed(userRepository, rado, username2, amount, reason).execute();
        String expected = "The amount cannot be negative " + System.lineSeparator();

        assertEquals(expected, result, "Result should be " + expected );
    }

    @Test
    void testExecuteWhenPaymentIsSuccessful() throws NegativeAmountException, UserNotFoundException, FriendNotFoundException {
        String username1 = "rado";
        String username2 = "kiro";
        double amount = 5;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        User kiro = userRepository.getUser(username2);
        userRepository.split(username1, kiro, amount, reason);
        String result = new Payed(userRepository, rado, username2, amount, reason).execute();
        String expected = "kiro has payed you 5.0 LV" + System.lineSeparator();

        assertEquals(expected, result, "Result should be" + expected);
    }
}
