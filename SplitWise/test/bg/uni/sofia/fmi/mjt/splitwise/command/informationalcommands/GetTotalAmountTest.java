package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroupException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetTotalAmountTest {
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
    public void testExecuteForFriendOwedTo()
        throws NegativeAmountException, UserNotFoundException, FriendNotFoundException {
        String friend = "dobo";
        boolean isGroup = false;
        boolean isOwedTo = true;
        User rado = userRepository.getUser("rado");
        userRepository.split(friend,rado,100,"reason");

        Command getTotalAmountCommand = new GetTotalAmount(userRepository, rado, friend, isGroup, isOwedTo);

        String result = getTotalAmountCommand.execute();

        String expected = "You owe 50.0 in total" + System.lineSeparator();
        assertEquals(expected, result, "Incorrect execution result");
    }

    @Test
    public void testExecuteForGroupOwedBy()
        throws UserNotFoundException, GroupAlreadyExistsException, NegativeAmountException, NotMemberOfGroupException,
        GroupNotFoundException {
        boolean isGroup = true;
        boolean isOwedTo = true;
        User rado = userRepository.getUser("rado");
        userRepository.createGroup(Set.of("rado","dobo","gosho"),"group");
        userRepository.splitGroup("group",rado,90,"reason");


        Command getTotalAmountCommand = new GetTotalAmount(userRepository, rado, "group", isGroup, isOwedTo);

        String result = getTotalAmountCommand.execute();

        String expected = "You owe 30.0 in total" + System.lineSeparator();
        assertEquals(expected, result, "Incorrect execution result");
    }
}
