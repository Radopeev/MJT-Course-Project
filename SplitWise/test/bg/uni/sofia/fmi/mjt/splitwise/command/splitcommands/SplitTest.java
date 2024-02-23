package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
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

public class SplitTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException, UsernameAlreadyTakenException, UserNotFoundException, AlreadyFriendsException {
        userRepository = new UserRepository(userFile,groupsFile);
        String username1 = "dobo";
        String username2 = "gosho";
        String username3 = "rado";
        String password1 = "dobo";
        String password2 = "gosho";
        String password3 = "rado";
        userRepository.addUser(username1, password1);
        userRepository.addUser(username2, password2);
        userRepository.addUser(username3, password3);
        userRepository.addFriendToUser(userRepository.getUser(username3),username1);
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecuteWhenFriendIsNotFound(){
        String username1 = "rado";
        String notFoundFriendUsername = "gosho";
        double amount = 10;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String expected = "You are not friends with gosho" + System.lineSeparator();
        String result = new Split(userRepository ,rado, notFoundFriendUsername, amount, reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void  testExecuteWhenAmountIsNegative(){
        String username1 = "rado";
        String username2 = "gosho";
        double negativeAmount = -10;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String expected = "The amount cannot be negative " + System.lineSeparator();
        String result = new Split(userRepository, rado, username2, negativeAmount, reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void  testExecute(){
        String username1 = "rado";
        String username2 = "dobo";
        double amount = 10;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String expected = "You have split 10.0 LV with dobo for lemons" + System.lineSeparator();
        String result = new Split(userRepository, rado, username2, amount, reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }
}
