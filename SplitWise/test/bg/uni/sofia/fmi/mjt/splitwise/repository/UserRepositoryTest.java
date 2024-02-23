package bg.uni.sofia.fmi.mjt.splitwise.repository;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroupException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.notification.FriendApprovedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.FriendOwedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.GroupApprovedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.GroupOwedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.Notification;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.SplitHistoryEntry;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {
    private UserRepository userRepository;
    private Path userFile =
        Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\users.dat");
    private Path groupsFile =
        Path.of("E:\\IdeaProjects\\Modern Java Technologies\\Split(NotSo)Wise\\SplitWise\\test\\groups.dat");

    @BeforeEach
    void setup() throws IOException {
        userRepository = new UserRepository(userFile, groupsFile);
    }

    @AfterEach
    void removeFiles() throws IOException {
        Files.delete(userFile);
        Files.delete(groupsFile);
    }

    @Test
    void testAddUserWhenUsernameIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.addUser(null, "rado"),
            "Adding user with null username should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> userRepository.addUser("", "rado")
            , "Adding user with blank username should throw IllegalArgumentException");
    }

    @Test
    void testAddUserWhenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.addUser("rado", null)
            , "Adding user with null password should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> userRepository.addUser("rado", "")
            , "Adding user with blank password should throw IllegalArgumentException");
    }

    @Test
    void testAddUserWhenUserAlreadyExists() throws UsernameAlreadyTakenException {
        userRepository.addUser("kiro", "kiro");
        assertThrows(UsernameAlreadyTakenException.class, () -> userRepository.addUser("kiro", "kiro"),
            "Adding user with an existing username should throw UsernameAlreadyTakenException");
    }

    @Test
    void testAddUser() throws UsernameAlreadyTakenException {
        userRepository.addUser("rado", "rado");
        assertTrue(userRepository.getUsers().containsKey("rado"),
            "Users map should contain key 'rado'");
        assertTrue(userRepository.getUsers().containsValue(new User("rado", "rado")),
            "Users map should contain user 'rado'");
    }

    @Test
    void testFindUserWhenUsernameIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.findUser(null, "rado"),
            "FindUser should throw an exception when username is null");
        assertThrows(IllegalArgumentException.class, () -> userRepository.findUser("", "rado"),
            "FindUser should throw an exception when username is blank");
    }

    @Test
    void testFindUserWhenPasswordIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.findUser("rado", null),
            "FindUser should throw an exception when username is null");
        assertThrows(IllegalArgumentException.class, () -> userRepository.findUser("rado", ""),
            "FindUser should throw an exception when username is blank");
    }

    @Test
    void testFindUserWhenUserIsNotFound() {
        assertThrows(UserNotFoundException.class, () -> userRepository.findUser("petko", "petko"),
            "FindUser should throw an exception when user is not found");
        assertThrows(UserNotFoundException.class, () -> userRepository.findUser("rado", "Rado"),
            "FindUser should throw an exception when username is found but password is incorrect");
    }

    @Test
    void testAddFriendToUserWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.addFriendToUser(null, "dobo"),
            "Adding a friend should throw an exception when the user is null");
    }

    @Test
    void testAddFriendToUserWhenFriendIsInvalid() {
        User user = new User("user", "user");
        assertThrows(IllegalArgumentException.class, () -> userRepository.addFriendToUser(user, null),
            "Adding a friend should throw an exception when the friend is null");
        assertThrows(IllegalArgumentException.class, () -> userRepository.addFriendToUser(user, ""),
            "Adding a friend should throw an exception when the friend is blank");
    }

    @Test
    void testAddFriend() throws UsernameAlreadyTakenException, UserNotFoundException, AlreadyFriendsException {
        userRepository.addUser("rado", "rado");
        userRepository.addUser("dobo", "dobo");
        User rado = userRepository.getUser("rado");
        User dobo = userRepository.getUser("dobo");
        userRepository.addFriendToUser(rado, "dobo");

        assertTrue(rado.getFriends().contains("dobo"), "Rado should have dobo as a friend");
        assertTrue(dobo.getFriends().contains("rado"), "Dobo should have rado as a friend");
    }

    @Test
    void testCreateGroupWhenGroupNameIsInvalid() {
        Set<String> members = new LinkedHashSet<>();

        assertThrows(IllegalArgumentException.class, () -> userRepository.createGroup(members, ""),
            "Creating a group should throw an exception when the group name is blank");
        assertThrows(IllegalArgumentException.class, () -> userRepository.createGroup(members, null),
            "Creating a group should throw an exception when the group name is null");
    }

    @Test
    void testCreateGroupWhenMembersIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.createGroup(null, "group1"),
            "Creating a group with null members should throw an IllegalArgumentException");
    }

    @Test
    void testCreateGroup() throws GroupAlreadyExistsException, UserNotFoundException {
        Set<String> members = new LinkedHashSet<>();
        String group = "group";
        userRepository.createGroup(members, group);

        assertTrue(userRepository.getGroups().containsKey(group), "Group should be added to the repository");

        Group createdGroup = userRepository.getGroups().get(group);
        assertNotNull(createdGroup, "The created group should not be null");
        assertEquals(group, createdGroup.getGroupName(), "Group name should match");
        assertTrue(createdGroup.getMembers().isEmpty(), "Group members should be empty initially");

    }

    @Test
    void testCreateGroupWhenGroupAlreadyExists() throws GroupAlreadyExistsException, UserNotFoundException {
        Set<String> members = new LinkedHashSet<>();
        String repeatedGroupName = "repeatedGroup";
        userRepository.createGroup(members, repeatedGroupName);
        assertThrows(GroupAlreadyExistsException.class, () -> userRepository.createGroup(members, repeatedGroupName),
            "Creating a group with an existing name ('" + repeatedGroupName +
                "') should throw GroupAlreadyExistsException");
    }

    @Test
    void testSplitWhenReasonIsInvalid() {
        User user2 = new User("user2", "user2");
        assertThrows(IllegalArgumentException.class, () -> userRepository.split("user1", user2, 5.0, ""),
            "Splitting expenses should throw an exception when the reason is blank");
        assertThrows(IllegalArgumentException.class, () -> userRepository.split("user1", user2, 5.0, null),
            "Splitting expenses should throw an exception when the reason is null");
    }

    @Test
    void testSplitWhenAmountIsNegative() throws UsernameAlreadyTakenException {
        userRepository.addUser("user1", "user1");
        userRepository.addUser("user2", "user2");
        User user2 = userRepository.getUser("user2");

        assertThrows(NegativeAmountException.class, () -> userRepository.split("user1", user2, -5.0, "lemons"),
            "Splitting expenses with a negative amount should throw NegativeAmountException");
    }

    @Test
    void testSplitWhenUsersAreNotFriends()
        throws UsernameAlreadyTakenException {
        userRepository.addUser("user1", "user1");
        userRepository.addUser("user2", "user2");
        User user2 = userRepository.getUser("user2");


        assertThrows(FriendNotFoundException.class, () -> userRepository.split("user1", user2, 5.0, "lemons"),
            "Splitting expenses between non-friends should throw FriendNotFoundException");
    }

    @Test
    void testSplit()
        throws UserNotFoundException, AlreadyFriendsException, NegativeAmountException, FriendNotFoundException,
        UsernameAlreadyTakenException {
        userRepository.addUser("user1", "user1");
        userRepository.addUser("user2", "user2");
        User user1 = userRepository.getUser("user1");
        User user2 = userRepository.getUser("user2");
        userRepository.addFriendToUser(user1, "user2");

        userRepository.split("user2", user1, 10, "lemons");

        assertTrue(user1.getOwedByFriendDueAmounts("user2").containsKey("lemons")
                && user1.getOwedByFriendDueAmounts("user2").get("lemons").equals(5.0)
                && user1.getOwedToFriendDueAmounts("user2").containsKey("lemons")
                && user1.getOwedToFriendDueAmounts("user2").get("lemons").equals(5.0),
            "User1's friend list should be updated after split");
        assertTrue(user2.getOwedByFriendDueAmounts("user1").containsKey("lemons")
                && user2.getOwedByFriendDueAmounts("user1").get("lemons").equals(5.0)
                && user2.getOwedToFriendDueAmounts("user1").containsKey("lemons")
                && user2.getOwedToFriendDueAmounts("user1").get("lemons").equals(5.0),
            "User1's friend list should be updated after split");

        Notification notification = new FriendOwedMoney("user1", "lemons", 5.0);
        assertTrue(user2.getFriendsNotifications().contains(notification),
            "User2's notifications should contain the split notification");
    }

    @Test
    void testSplitGroupWhenGroupNameIsInvalid() {
        User user1 = userRepository.getUser("user1");
        assertThrows(IllegalArgumentException.class, () -> userRepository.splitGroup("", user1, 5.0, "dog"),
            "Splitting group expenses should throw an exception when the group name is blank");
        assertThrows(IllegalArgumentException.class, () -> userRepository.splitGroup(null, user1, 5.0, "dog"),
            "Splitting group expenses should throw an exception when the group name is null");
    }

    @Test
    void testSplitGroupWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.splitGroup("group1", null, 5.0, "dog"),
            "Splitting group expenses should throw an exception when the user is null");
    }

    @Test
    void testSplitGroupWhenAmountIsNegative() {
        User user1 = new User("user1", "user1");
        assertThrows(NegativeAmountException.class, () -> userRepository.splitGroup("group", user1, -5.0, "dog"),
            "Splitting group expenses with a negative amount should throw NegativeAmountException");
    }

    @Test
    void testSplitGroupWhenReasonIsInvalid() {
        User user1 = new User("user1", "user1");
        assertThrows(IllegalArgumentException.class, () -> userRepository.splitGroup("group1", user1, 5.0, ""),
            "Splitting group expenses should throw an exception when the reason is blank");
        assertThrows(IllegalArgumentException.class, () -> userRepository.splitGroup("group1", user1, 5.0, null),
            "Splitting group expenses should throw an exception when the reason is null");
    }

    @Test
    void testSplitGroupWhenGroupIsNotPresent() {
        User user1 = new User("user1", "user1");
        assertThrows(GroupNotFoundException.class, () -> userRepository.splitGroup("group", user1, 5.0, "lemons"),
            "Splitting group expenses for a non-existent group should throw GroupNotFoundException");
    }

    @Test
    void testSplitGroupWhenUserIsNotMemberOfTheGroup()
        throws UsernameAlreadyTakenException, GroupAlreadyExistsException, UserNotFoundException {
        User user1 = new User("user1", "user1");
        userRepository.addUser("user2", "user2");
        userRepository.addUser("user3", "user3");
        userRepository.addUser("user4", "user4");
        Set<String> members = Set.of("user2", "user3", "user4");
        userRepository.createGroup(members, "group1");

        assertThrows(NotMemberOfGroupException.class, () -> userRepository.splitGroup("group1", user1, 5.0, "lemons"),
            "Splitting group expenses for a user not a member of the group should throw NotMemberOfGroupException");
    }

    @Test
    void testSplitGroup()
        throws UsernameAlreadyTakenException, GroupAlreadyExistsException, NegativeAmountException,
        GroupNotFoundException,
        NotMemberOfGroupException, UserNotFoundException {
        String groupName = "testGroup";
        String username1 = "user1";
        String username2 = "user2";
        String username3 = "user3";
        double amount = 99.0;
        double amountPerEach = 33.0;
        String reason = "expense";

        userRepository.addUser(username1, "password1");
        userRepository.addUser(username2, "password2");
        userRepository.addUser(username3, "password3");
        Set<String> members = new LinkedHashSet<>();
        members.add(username1);
        members.add(username2);
        members.add(username3);

        userRepository.createGroup(members, groupName);
        Notification n1 = new GroupOwedMoney(reason, amountPerEach, groupName, "user3,user1");
        Notification n2 = new GroupOwedMoney(reason, amountPerEach, groupName, "user2,user1");
        userRepository.splitGroup(groupName, userRepository.getUser(username1), amount, reason);

        assertTrue(userRepository.getUser(username2).getGroupsNotifications().get(groupName).contains(n1),
            "User2's group notifications should contain the split notification for user1 and user3");
        assertTrue(userRepository.getUser(username3).getGroupsNotifications().get(groupName).contains(n2),
            "User3's group notifications should contain the split notification for user1 and user2");

        assertTrue(userRepository.getUser(username1).getOwedByGroupDueAmounts(groupName).containsKey(reason) &&
                userRepository.getUser(username1).getOwedByGroupDueAmounts(groupName).containsKey(reason) &&
                userRepository.getUser(username1).getOwedByGroupDueAmounts(groupName).get(reason).equals(66.0) &&
                userRepository.getUser(username1).getOwedToGroupDueAmounts(groupName).get(reason).equals(33.0),
            "User1's group balance should be updated after split");
        assertTrue(userRepository.getUser(username2).getOwedByGroupDueAmounts(groupName).containsKey(reason) &&
                userRepository.getUser(username2).getOwedByGroupDueAmounts(groupName).containsKey(reason) &&
                userRepository.getUser(username2).getOwedByGroupDueAmounts(groupName).get(reason).equals(66.0) &&
                userRepository.getUser(username2).getOwedToGroupDueAmounts(groupName).get(reason).equals(33.0),
            "User2's group balance should be updated after split");
        assertTrue(userRepository.getUser(username3).getOwedByGroupDueAmounts(groupName).containsKey(reason) &&
                userRepository.getUser(username3).getOwedByGroupDueAmounts(groupName).containsKey(reason) &&
                userRepository.getUser(username3).getOwedByGroupDueAmounts(groupName).get(reason).equals(66.0) &&
                userRepository.getUser(username3).getOwedToGroupDueAmounts(groupName).get(reason).equals(33.0),
            "User3's group balance should be updated after split");
    }

    @Test
    void testPayedWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.payed("user", 2.0, null, "lemons"),
            "Recording a payment should throw an exception when the user is null");
    }

    @Test
    void testPayedWhenPayingUserIsNull() {
        User user = new User("user", "user");

        assertThrows(IllegalArgumentException.class, () -> userRepository.payed(null, 2.0, user, "lemons"),
            "Recording a payment should throw an exception when the paying user is null");
    }

    @Test
    void testPayedWhenAmountIsNegative() {
        User user1 = new User("user1", "user1");

        assertThrows(NegativeAmountException.class, () -> userRepository.payed("user2", -2.0, user1, "lemons"),
            "Recording a payment should throw NegativeAmountException when the amount is negative");
    }

    @Test
    void testPayedWhenReasonIsNullOrBlank() {
        User user1 = new User("user1", "user1");

        assertThrows(IllegalArgumentException.class, () -> userRepository.payed("user2", 2.0, user1, ""),
            "Recording a payment should throw an exception when the reason is blank");

        assertThrows(IllegalArgumentException.class, () -> userRepository.payed("user2", 2.0, user1, null),
            "Recording a payment should throw an exception when the reason is null");
    }

    @Test
    void testPayedWhenFriendIsNotFound() throws UsernameAlreadyTakenException {
        userRepository.addUser("user1", "user2");
        userRepository.addUser("user2", "user2");
        User user1 = userRepository.getUser("user1");

        assertThrows(FriendNotFoundException.class, () -> userRepository.payed("user2", 2.0, user1, "lemons"),
            "Recording a payment should throw FriendNotFoundException when the friend is not found");
    }

    @Test
    public void testPayed()
        throws FriendNotFoundException, NegativeAmountException, UsernameAlreadyTakenException,
        NoDueAmountFoundException,
        UserNotFoundException,
        AlreadyFriendsException {
        String payerUsername = "payer";
        String userToPayUsername = "userToPay";
        double amount = 50.0;
        String reason1 = "lemons";
        String reason2 = "oranges";

        userRepository.addUser(payerUsername, "password1");
        userRepository.addUser(userToPayUsername, "password2");

        User payer = userRepository.getUser(payerUsername);
        User userToPay = userRepository.getUser(userToPayUsername);
        userRepository.addFriendToUser(userToPay, payerUsername);
        userRepository.split(userToPayUsername, payer, amount, reason1);
        userRepository.split(userToPayUsername, payer, amount, reason2);

        userRepository.payed(payerUsername, 10, userToPay, reason1);
        userRepository.payed(payerUsername, 25, userToPay, reason2);
        Notification notification1 = new FriendApprovedMoney(userToPayUsername, reason1, 10);
        Notification notification2 = new FriendApprovedMoney(userToPayUsername, reason2, 25);

        assertEquals(15.0, payer.getOwedToFriendDueAmounts(userToPayUsername).get(reason1),
            "Friend balance should be updated after the first payment");
        assertEquals(25.0, payer.getOwedByFriendDueAmounts(userToPayUsername).get(reason1),
            "Friend balance should be updated after the first payment");
        assertTrue(payer.getOwedToFriendDueAmounts(userToPayUsername).containsKey(reason1),
            "Friend balance should be present for the first reason");
        assertFalse(payer.getOwedToFriendDueAmounts(userToPayUsername).containsKey(reason2),
            "Friend balance should not be present for the second reason");

        assertTrue(payer.getFriendsNotifications().containsAll(List.of(notification1, notification2)),
            "Friend notifications should contain both payment notifications");
    }

    @Test
    void testPayedGroupWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> userRepository.payedGroup("user", "group,", null, 1.0, "lemons"),
            "Recording a group payment should throw an exception when the user is null");
    }

    @Test
    void testPayedGroupWhenUserPayingUserIsNull() {
        User user = new User("user", "user");
        assertThrows(IllegalArgumentException.class,
            () -> userRepository.payedGroup(null, "group,", user, 1.0, "lemons"),
            "Recording a group payment should throw an exception when the paying user is null");
    }

    @Test
    void testPayedGroupWhenGroupNameIsBlankOrNull() {
        User user1 = new User("user1", "user1");

        assertThrows(IllegalArgumentException.class, () -> userRepository.payedGroup("user2", "", user1, 2.0, "lemons"),
            "Recording a group payment should throw an exception when the group name is blank");
        assertThrows(IllegalArgumentException.class,
            () -> userRepository.payedGroup("user2", null, user1, 2.0, "lemons"),
            "Recording a group payment should throw an exception when the group name is null");
    }

    @Test
    void testPayedGroupWhenAmountIsNegative() throws UsernameAlreadyTakenException {
        userRepository.addUser("user1", "use1");
        userRepository.addUser("user2", "user2");
        User user1 = userRepository.getUser("user1");

        assertThrows(NegativeAmountException.class,
            () -> userRepository.payedGroup("user2", "group1", user1, -2.0, "lemons"),
            "Recording a group payment should throw NegativeAmountException when the amount is negative");
    }

    @Test
    void testPayedGroupWhenGroupIsNotPresent() throws UsernameAlreadyTakenException {
        userRepository.addUser("user1", "use1");
        userRepository.addUser("user2", "user2");
        User user2 = userRepository.getUser("user2");

        assertThrows(GroupNotFoundException.class,
            () -> userRepository.payedGroup("user1", "NonExisting", user2, 2.0, "lemons"),
            "Recording a group payment should throw GroupNotFoundException when the group is not found");
    }

    @Test
    void testPayedGroupWhenPayingUserIsNotMembersOfTheGroup() throws GroupAlreadyExistsException,
        UserNotFoundException, UsernameAlreadyTakenException {
        userRepository.addUser("user1", "use1");
        userRepository.addUser("user2", "user2");
        User user2 = userRepository.getUser("user2");
        userRepository.createGroup(Set.of("user2"), "group1");

        assertThrows(NotMemberOfGroupException.class,
            () -> userRepository.payedGroup("user1", "group1", user2, 2.0, "lemons"),
            "Recording a group payment should throw NotMemberOfGroupException when the paying user is not a member of the group");
    }

    @Test
    void testPayedGroupWhenUserIsNotMembersOfTheGroup()
        throws GroupAlreadyExistsException, UserNotFoundException, UsernameAlreadyTakenException {
        userRepository.addUser("user1", "use1");
        userRepository.addUser("user2", "user2");
        User user2 = userRepository.getUser("user2");
        userRepository.createGroup(Set.of("user1"), "group1");

        assertThrows(NotMemberOfGroupException.class,
            () -> userRepository.payedGroup("user1", "group1", user2, 2.0, "lemons"),
            "Recording a group payment should throw NotMemberOfGroupException when the user is not a member of the group");
    }

    @Test
    void testPayedGroup()
        throws GroupAlreadyExistsException, NegativeAmountException, GroupNotFoundException, NotMemberOfGroupException,
        NoDueAmountFoundException, UserNotFoundException, UsernameAlreadyTakenException {
        userRepository.addUser("user1", "user1");
        userRepository.addUser("user2", "user2");
        userRepository.createGroup(Set.of("user1", "user2"), "group1");
        User user1 = userRepository.getUser("user1");
        User user2 = userRepository.getUser("user2");
        String groupName = "group1";
        String reason = "expense";
        userRepository.splitGroup(groupName, user1, 12, reason);

        userRepository.payedGroup("user1", groupName, user2, 3, reason);
        Notification notification = new GroupApprovedMoney(user2.getUsername(), reason, 3, groupName);

        assertTrue(user1.getOwedToGroupDueAmounts(groupName).containsKey(reason)
                && user1.getOwedByGroupDueAmounts(groupName).containsKey(reason),
            "Group balance should be updated after the payment");
        assertEquals(3, user1.getOwedToGroupDueAmounts(groupName).get(reason),
            "Group balance should be updated to the correct amount");
        assertEquals(6, user1.getOwedByGroupDueAmounts(groupName).get(reason),
            "Group balance should be updated to the correct amount");
        assertTrue(user1.getGroupsNotifications().containsKey("group1")
                && user1.getGroupsNotifications().get("group1").contains(notification),
            "Group notifications should contain the payment notification");
    }

    @Test
    void testGetPaymentHistory() throws UsernameAlreadyTakenException {
        String username = "user";
        String password = "password";

        userRepository.addUser(username, password);
        User user = userRepository.getUser(username);
        SplitHistoryEntry expectedEntry = new SplitHistoryEntry("friendUsername", "TestReason", 50.0, false);
        user.addToPaymentHistory(expectedEntry);

        List<SplitHistoryEntry> paymentHistory = userRepository.getPaymentHistory(user);

        assertNotNull(paymentHistory, "Payment history should not be null");
        assertEquals(1, paymentHistory.size(), "Payment history size should be 1");

        SplitHistoryEntry actualEntry = paymentHistory.getFirst();
        assertEquals(expectedEntry.toString(), actualEntry.toString(), "Entry in payment history should match the expected entry");
    }

    @Test
    void testGetTotalAmountWhenUserIsNull(){
        assertThrows(IllegalArgumentException.class, () -> userRepository.getTotalAmount(null,"target",false,false));
    }

    @Test
    void testGetTotalAmountWhenTargetIsNullOrBlank() throws UsernameAlreadyTakenException {
        userRepository.addUser("user1","user2");
        assertThrows(IllegalArgumentException.class,
            () -> userRepository.getTotalAmount(userRepository.getUser("user1"),"",false,false));
        assertThrows(IllegalArgumentException.class,
            () -> userRepository.getTotalAmount(userRepository.getUser("user1"),null,false,false));
    }

    @Test
    void testGetTotalAmount() throws UsernameAlreadyTakenException {
        userRepository.addUser("user1","user1");
        User user = userRepository.getUser("user1");
        String group = "group1";
        double amountOwedTo = 50.0;
        double amountOwedBy = 30.0;
        String reason = "TestReason";

        user.addGroup(group);
        user.splitWithGroup(group, amountOwedTo, amountOwedBy, reason);

        double totalOwedByGroup = userRepository.getTotalAmount(user, group, true, false);

        assertEquals(amountOwedBy, totalOwedByGroup, 0.001, "Incorrect total owed by group");
    }
}
