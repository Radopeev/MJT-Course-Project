package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException {
        userRepository = new UserRepository(userFile,groupsFile);
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecute(){
        String result = new Register(userRepository,"dobo","dobo").execute();
        String expected = "You have successfully registered"+System.lineSeparator();

        assertEquals(expected,result,"Result should be " + expected);
    }
    @Test
    void testExecuteWhenUsernameIsAlreadyTaken() throws UsernameAlreadyTakenException {
        String username = "rado";
        String password= "rado";
        userRepository.addUser(username,password);
        String result = new Register(userRepository,username,password).execute();
        String expected = "User with this username already exists " + System.lineSeparator();

        assertEquals(expected,result, "Result should be " + expected);
    }
}
