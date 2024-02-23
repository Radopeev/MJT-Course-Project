package bg.uni.sofia.fmi.mjt.splitwise.group;

import static org.junit.jupiter.api.Assertions.*;

import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GroupTest {

    private Group group;
    private User user1, user2, user3;

    @BeforeEach
    void setUp() {
        user1 = new User("user1","password1");
        user2 = new User("user2","password2");
        user3 = new User("user3","password3");

        Set<User> members = new HashSet<>();
        members.add(user1);
        members.add(user2);
        members.add(user3);

        group = new Group(members, "testGroup");
    }

    @Test
    void testGetGroupName() {
        assertEquals("testGroup", group.getGroupName(), "Should return the correct group name");
    }

    @Test
    void testContainsUser() {
        assertTrue(group.containsUser(user1), "User1 should be in the group");
        assertTrue(group.containsUser(user2), "User2 should be in the group");
        assertTrue(group.containsUser(user3), "User3 should be in the group");

        User newUser = new User("newUser","newPassword");
        assertFalse(group.containsUser(newUser), "NewUser should not be in the group");
    }

    @Test
    void testGetMembers() {
        Set<User> members = group.getMembers();
        assertTrue(members.contains(user1), "User1 should be in the members set");
        assertTrue(members.contains(user2), "User2 should be in the members set");
        assertTrue(members.contains(user3), "User3 should be in the members set");
        assertEquals(3, members.size(), "Members set should have a size of 3");
    }

    @Test
    void testGetDueAmounts() {
        Map<User, Map<String, Double>> dueAmounts = group.getDueAmounts();
        assertEquals(3, dueAmounts.size(), "Due amounts map should have a size of 3");

        dueAmounts.forEach((user, amounts) -> {
            assertTrue(amounts.isEmpty(), "Initial due amounts map should be empty");
        });
    }

    @Test
    void testAddDueAmount() {
        group.addDueAmount(10.0, "expense1");

        Map<User, Map<String, Double>> dueAmounts = group.getDueAmounts();
        assertEquals(3, dueAmounts.size(), "Due amounts map should have a size of 3");

        dueAmounts.forEach((user, amounts) -> {
            assertTrue(amounts.containsKey("expense1"), "expense1 should be added to the due amounts");
            assertEquals(10.0, amounts.get("expense1"), "expense1 amount should be 10.0");
        });

        group.addDueAmount(5.0, "expense1");

        dueAmounts.forEach((user, amounts) -> {
            assertTrue(amounts.containsKey("expense1"), "expense1 should still be in the due amounts");
            assertEquals(15.0, amounts.get("expense1"), "expense1 amount should be updated to 15.0");
        });

        group.addDueAmount(8.0, "expense2");

        dueAmounts.forEach((user, amounts) -> {
            assertTrue(amounts.containsKey("expense1"), "expense1 should still be in the due amounts");
            assertEquals(15.0, amounts.get("expense1"), "expense1 amount should still be 15.0");
            assertTrue(amounts.containsKey("expense2"), "expense2 should be added to the due amounts");
            assertEquals(8.0, amounts.get("expense2"), "expense2 amount should be 8.0");
        });
    }
}


