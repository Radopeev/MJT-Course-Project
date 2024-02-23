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

public class HelpLoggedUserTest {
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
                To add new friend type :
                    add-friend <username>
                To create new group type :
                    create-group <group_name> <username> <username> ... <username>
                To split new bill with a friend type :
                    split <amount> <username> <reason_for_payment>
                To split new bill with a group type :
                    split-group <amount> <group_name> <reason_for_payment>
                To get your current status type :
                    get-status
                То update that someone has repaid you type :
                    payed <amount> <username> <reason>
                To update that someone has repaid you in a specific group type :
                    payed-group <groupname> <username> <amount> <reason>
                To disconnect from the app type :
                    quit
                To see the history of your splits type :
                    history
                To see how much are owed or owe to group or friend type :
                    get-total-amount <'group'/'friend'> <'owedto'/'owedby'> <groupname/username>
                """;
        String result = new HelpLoggedUser().execute();

        assertEquals(expected, result, "Result should be" + expected);
    }
}
