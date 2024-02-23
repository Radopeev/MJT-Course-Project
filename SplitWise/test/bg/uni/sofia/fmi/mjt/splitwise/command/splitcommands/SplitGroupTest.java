package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

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

public class SplitGroupTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup() throws IOException, UsernameAlreadyTakenException, UserNotFoundException, AlreadyFriendsException,
        GroupAlreadyExistsException {
        userRepository = new UserRepository(userFile,groupsFile);
        String username1 = "dobo";
        String username2 = "gosho";
        String username3 = "rado";
        String password1 = "dobo";
        String password2 = "gosho";
        String password3 = "rado";
        String groupName = "group1";
        userRepository.addUser(username1,password1);
        userRepository.addUser(username2,password2);
        userRepository.addUser(username3,password3);
        userRepository.addFriendToUser(userRepository.getUser(username3),username1);
        Set<String> members = Set.of(
            username3,username2
        );
        userRepository.createGroup(members,groupName);
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecuteWhenAmountIsNegative(){
        String username = "rado";
        double negativeAmount = -1;
        String reason = "lemons";
        String groupName = "group1";
        User rado = userRepository.getUser(username);
        String expected = "The amount cannot be negative ";
        String result = new SplitGroup(userRepository, rado, groupName, negativeAmount, reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecuteWhenUserIsNoMemberOfTheGroup(){
        User dobo = userRepository.getUser("dobo");
        String expected = "You are not a member of this group ";
        String result = new SplitGroup(userRepository,dobo,"group1",1.0,"lemons").execute();

        assertEquals(expected,result,"Result should be " + expected);
    }

    @Test
    void testExecuteWhenGroupIsNotPresent(){
        String username = "dobo";
        double amount = 1;;
        String nonExistingGroupName = "group2";
        String reason = "lemons";
        User dobo = userRepository.getUser(username);
        String expected = "group2 does not exists";
        String result = new SplitGroup(userRepository, dobo, nonExistingGroupName, amount, reason).execute();

        assertEquals(expected, result, "Result should be" + expected);
    }

    @Test
    void testExecute(){
        String username = "rado";
        String groupName = "group1";
        double amount = 2;
        String reason = "lemons";
        User rado = userRepository.getUser(username);
        String expected =  "You have split 2.0 LV with group1 for lemons" + System.lineSeparator();
        String result = new SplitGroup(userRepository, rado, groupName, amount, reason).execute();

        assertEquals(expected,result, "Result should be " + expected);
    }
}
