package bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateGroupTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException, UsernameAlreadyTakenException, UserNotFoundException, AlreadyFriendsException {
        userRepository = new UserRepository(userFile,groupsFile);
        userRepository.addUser("dobo","dobo");
        userRepository.addUser("gosho","gosho");
        userRepository.addUser("rado","rado");
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecuteWhenGroupAlreadyExists() throws GroupAlreadyExistsException, UserNotFoundException {
        String username1 = "rado";
        String username2 = "dobo";
        String username3 = "gosho";
        String groupName = "group1";
        User rado = userRepository.getUser(username1);
        Set<String> members = Set.of(username2, username3);
        userRepository.createGroup(members, groupName);
        String result = new CreateGroup(userRepository, rado, members, groupName).execute();
        String expected = "Group with this name already exists " + System.lineSeparator();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecute(){
        String username1 = "rado";
        String username2 = "dobo";
        String username3 = "gosho";
        String groupName = "group2";
        User rado = userRepository.getUser(username1);
        Set<String> members = Set.of(username2, username3);
        String result = new CreateGroup(userRepository, rado, members, groupName).execute();
        String expected = "You have successfully created group2 group" + System.lineSeparator();

        assertEquals(expected, result, "Result should be " + expected);
    }
}
