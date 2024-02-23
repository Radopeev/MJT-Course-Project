package bg.uni.sofia.fmi.mjt.splitwise.database;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {
    private static Path usersFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\usersFile.dat");
    private static Path groupsFile = Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groupsFile.dat");
    @AfterAll
    static void removeFiles() throws IOException {
        Files.delete(usersFile);
        Files.delete(groupsFile);
    }
    @Test
    void readUsers_nonEmptyFile_shouldReturnPopulatedMap()
        throws IOException, UsernameAlreadyTakenException, UserNotFoundException, GroupAlreadyExistsException {
        UserRepository userRepository = new UserRepository(usersFile, groupsFile);
        userRepository.addUser("user1","user1");
        userRepository.addUser("user2","user2");
        userRepository.createGroup(Set.of("user1","user2"),"group1");
        Group group = new Group(Set.of(
            userRepository.getUser("user1"),
            userRepository.getUser("user2")),"group1");
        DatabaseSaver.writeDataToDatabase(usersFile, groupsFile, userRepository);

        Map<String, User> users = DatabaseReader.readUsers(usersFile);
        Map<String, Group> groups = DatabaseReader.readGroups(groupsFile);

        assertEquals(2, users.size(),"Users size should be 2");
        assertEquals(userRepository.getUser("user1"), users.get("user1"),"User1 should be the same in both maps");
        assertEquals(userRepository.getUser("user2"), users.get("user2"),"User2 should be the same in both maps");
        assertTrue(groups.containsKey("group1"),"Groups map should have key 'group1'");
        assertEquals(userRepository.getGroups().get("group1").getMembers(), group.getMembers(),
            "Members should be the same in both groups");
    }
}



