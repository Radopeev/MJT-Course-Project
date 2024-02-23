package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

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

public class AddFriendTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException, UsernameAlreadyTakenException, UserNotFoundException, AlreadyFriendsException {
        userRepository = new UserRepository(userFile,groupsFile);
        userRepository.addUser("dobo","dobo");
        userRepository.addUser("gosho","gosho");
        userRepository.addUser("rado","rado");
        User user = userRepository.getUser("dobo");
        userRepository.addFriendToUser(user,"gosho");
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecuteWhenUserIsNotFound() {
        String username1 = "rado";
        String username2 = "petko";
        User user = userRepository.getUser(username1);
        String result = new AddFriend(userRepository,user,username2).execute();
        String expected = "User with petko not found " + System.lineSeparator();

        assertEquals(expected, result, "Result should be" + expected);
    }

    @Test
    void testExecuteWhenUsersAreAlreadyFriends() {
        String username1 = "dobo";
        String username2 = "gosho";
        User user = userRepository.getUser(username1);
        String result = new AddFriend(userRepository,user,username2).execute();
        String expected = "You are already friends with gosho"+System.lineSeparator();

        assertEquals(expected, result, "Result should be" + expected);
    }

    @Test
    void testExecute(){
        String username1 = "rado";
        String username2 = "dobo";
        User rado = userRepository.getUser(username1);
        String result = new AddFriend(userRepository,rado,username2).execute();
        String expected = "You have successfully added dobo as your friend" + System.lineSeparator();

        assertEquals(expected, result, "Result should be " + expected);
    }
}
