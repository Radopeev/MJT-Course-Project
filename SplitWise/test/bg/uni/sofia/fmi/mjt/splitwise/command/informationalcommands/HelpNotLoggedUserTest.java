package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpNotLoggedUserTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException, UsernameAlreadyTakenException, UserNotFoundException,
        AlreadyFriendsException {
        userRepository = new UserRepository(userFile,groupsFile);
        userRepository.addUser("dobo","dobo");
        userRepository.addUser("gosho","gosho");
        userRepository.addUser("rado","rado");
        userRepository.addFriendToUser(userRepository.getUser("rado"),"dobo");
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecute() {
        String expected = """
            To register type: register <username> <password>,
            To login type: login <username> <password>;
        """;
        String result = new HelpNotLoggedUser().execute();

        assertEquals(expected, result, "Result should be" + expected);
    }
}
