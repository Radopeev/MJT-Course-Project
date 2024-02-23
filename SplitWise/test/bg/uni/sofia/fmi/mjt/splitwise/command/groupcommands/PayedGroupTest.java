package bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayedGroupTest {
    private static UserRepository userRepository;
    private static Path userFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeAll
    static void setup()
        throws IOException, UsernameAlreadyTakenException, GroupAlreadyExistsException, UserNotFoundException {
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
        Set<String> members = Set.of(username3, username2);
        userRepository.createGroup(members, groupName);
    }

    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testExecuteWhenAmountIsNegative(){
        String username1 = "rado";
        String username2 = "dobo";
        String groupName = "group1";
        double negativeAmount = -1.0;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String result = new PayedGroup(userRepository,rado,groupName,username2,negativeAmount,reason).execute();
        String expected = "The amount cannot be negative " + System.lineSeparator();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecuteWhenGroupIsNotPresent(){
        String username1 = "rado";
        String username2 = "dobo";
        String nonExistingGroupName = "group2";
        double amount = 1.0;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String expected =  "group2 does not exists" + System.lineSeparator();
        String result = new PayedGroup(userRepository,rado,nonExistingGroupName,username2,amount,reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecuteWhenUserIsNotMember(){
        String username1 = "rado";
        String username2 = "dobo";
        String groupName = "group1";
        double amount = 1.0;
        String reason = "lemons";
        User dobo = userRepository.getUser(username2);
        String expected =  "You are not a member of this group " + System.lineSeparator();
        String result = new PayedGroup(userRepository,dobo,groupName,username1,amount,reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecuteWhenPayingUserIsNotMember(){
        String username1 = "rado";
        String username2 = "dobo";
        double amount = 1.0;
        String reason = "lemons";
        String groupName = "group1";
        User rado = userRepository.getUser(username1);
        String expected = "dobo is not a member of this group " + System.lineSeparator();
        String result = new PayedGroup(userRepository, rado, groupName, username2, amount, reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecuteWhenDueAmountDoesNotExists(){
        String username1 = "rado";
        String username2 = "gosho";
        double amount = 1.0;
        String groupName = "group1";
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        String expected = "You don't owe money for lemons" + System.lineSeparator();
        String result = new PayedGroup(userRepository,rado,groupName,username2,amount,reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }

    @Test
    void testExecute() throws NegativeAmountException, GroupNotFoundException, NotMemberOfGroupException {
        String username1 = "rado";
        String username2 = "gosho";
        String group1 = "group1";
        double amount = 10;
        String reason = "lemons";
        User rado = userRepository.getUser(username1);
        userRepository.splitGroup(group1, rado, amount, reason);
        String expected = "gosho has payed 10.0 LV [lemons] in group1" + System.lineSeparator();
        String result = new PayedGroup(userRepository, rado, group1, username2, amount, reason).execute();

        assertEquals(expected, result, "Result should be " + expected);
    }
}