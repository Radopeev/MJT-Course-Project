package bg.uni.sofia.fmi.mjt.splitwise.repository;

import bg.uni.sofia.fmi.mjt.splitwise.database.DatabaseReader;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedByException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedToException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroupException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.formatter.DoubleFormatter;
import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.notification.FriendApprovedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.FriendOwedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.GroupApprovedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.GroupOwedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.Notification;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.PaymentType;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.SplitHistoryEntry;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRepository implements Repository {
    private final Map<String, User> users;
    private final Map<String, Group> groups;
    private static final String REASON = "Reason";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String FRIEND = "Friend";
    private static final String MEMBER = "Member";
    private static final String GROUP_NAME = "Group name";
    private static final String PAYING_USER_USERNAME = "Paying user username";
    private static final int HALF = 2;

    public UserRepository(Path usersFile, Path groupsFile) throws IOException {
        users = DatabaseReader.readUsers(usersFile);
        groups = DatabaseReader.readGroups(groupsFile);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public User getUser(String username) {
        return users.get(username);
    }

    private void assertStringIsNotNullOrBlank(String str, String problematic) {
        if (str == null || str.isBlank()) {
            throw  new IllegalArgumentException(problematic + " cannot be null or blank ");
        }
    }

    private void assertUserIsNotNull(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null ");
        }
    }

    private void assertMembersIsNotNull(Set<String> members) {
        if (members == null) {
            throw new IllegalArgumentException("Members cannot be null");
        }
    }

    private void assertUsernameIsNotAlreadyTaken(String username) throws UsernameAlreadyTakenException {
        if (users.containsKey(username)) {
            throw new UsernameAlreadyTakenException("User with this username already exists ");
        }
    }

    private void assertUserIsRegistered(String username, String password) throws UserNotFoundException {
        if (users.get(username) == null || !users.get(username).isPasswordCorrect(password)) {
            throw new UserNotFoundException("You are not registered");
        }
    }

    private void assertUserIsFound(String username) throws UserNotFoundException {
        if (users.get(username) == null) {
            throw new UserNotFoundException("User with " + username + " not found ");
        }
    }

    private void assertUsersArentAlreadyFriends(User user, String friend) throws AlreadyFriendsException {
        if (users.get(user.getUsername()).getFriends().contains(friend)) {
            throw new AlreadyFriendsException("You are already friends with " + friend);
        }
    }

    private void assertDoesNotGroupAlreadyExists(String groupName) throws GroupAlreadyExistsException {
        if (groups.containsKey(groupName)) {
            throw new GroupAlreadyExistsException("Group with this name already exists ");
        }
    }

    private void assertPositiveAmount(double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("The amount cannot be negative ");
        }
    }

    private void assertFriendIsFound(User user, User userToSplitWith) throws FriendNotFoundException {
        if (!user.getFriends().contains(userToSplitWith.getUsername())) {
            throw new FriendNotFoundException("You are not friends with " + userToSplitWith.getUsername());
        }
    }

    private void assertGroupIsFound(String groupName) throws GroupNotFoundException {
        if (!groups.containsKey(groupName)) {
            throw new GroupNotFoundException(groupName + " does not exists");
        }
    }

    private void assertUserIsMemberOfGroup(String groupName, User user) throws NotMemberOfGroupException {
        if (!groups.get(groupName).containsUser(user)) {
            throw new NotMemberOfGroupException("You are not a member of this group ");
        }
    }

    private void assertPayingUserIsMemberOfGroup(String groupName, User payingUser) throws NotMemberOfGroupException {
        if (!groups.get(groupName).containsUser(payingUser)) {
            throw new NotMemberOfGroupException(payingUser.getUsername() + " is not a member of this group ");
        }
    }

    private void sendFriendNotification(User user, Notification notification) {
        user.addFriendNotification(notification);
    }

    private void sendGroupNotification(User user, String groupName, Notification notification) {
        user.addGroupNotification(groupName, notification);
    }

    private void sendGroupNotificationToAllMembers(String groupName, double amountPerEach, String reason, User user) {
        for (User curr: groups.get(groupName).getMembers()) {
            if (!curr.equals(user)) {
                sendGroupNotification(curr, groupName, new GroupOwedMoney(reason, amountPerEach, groupName,
                    groups.get(groupName).getMembers().stream().filter( u -> !u.equals(curr))
                        .map(User::getUsername).collect(Collectors.joining(","))));
            }
        }
    }

    @Override
    public void addUser(String username, String password) throws UsernameAlreadyTakenException {
        assertStringIsNotNullOrBlank(username, USERNAME);
        assertStringIsNotNullOrBlank(password, PASSWORD);
        assertUsernameIsNotAlreadyTaken(username);

        users.put(username, new User(username, password));
    }

    @Override
    public void findUser(String username, String password) throws UserNotFoundException {
        assertStringIsNotNullOrBlank(username, USERNAME);
        assertStringIsNotNullOrBlank(password, PASSWORD);
        assertUserIsRegistered(username, password);
    }

    @Override
    public void addFriendToUser(User user, String friend) throws UserNotFoundException, AlreadyFriendsException {
        assertUserIsNotNull(user);
        assertStringIsNotNullOrBlank(friend, FRIEND);
        assertUserIsFound(friend);
        assertUserIsFound(friend);
        assertUsersArentAlreadyFriends(user, friend);

        user.addFriend(friend);
        users.get(friend).addFriend(user.getUsername());
    }

    @Override
    public void createGroup(Set<String> membersUsernames, String groupName)
        throws GroupAlreadyExistsException, UserNotFoundException {
        assertMembersIsNotNull(membersUsernames);
        for (String member: membersUsernames) {
            assertStringIsNotNullOrBlank(member, MEMBER);
            assertUserIsFound(member);
        }
        assertStringIsNotNullOrBlank(groupName, GROUP_NAME);
        assertDoesNotGroupAlreadyExists(groupName);


        membersUsernames.forEach(u -> users.get(u).addGroup(groupName));
        Set<User> members = new HashSet<>();
        for(String member: membersUsernames){
            members.add(users.get(member));
        }
        groups.put(groupName, new Group(members, groupName));
    }

    @Override
    public void split(String friendUsername, User user, double amount, String reason)
        throws FriendNotFoundException, NegativeAmountException, UserNotFoundException {
        assertStringIsNotNullOrBlank(reason, REASON);
        assertStringIsNotNullOrBlank(friendUsername, FRIEND + " " + USERNAME);
        assertUserIsNotNull(user);
        assertUserIsFound(friendUsername);
        User friend = users.get(friendUsername);
        assertPositiveAmount(amount);
        assertFriendIsFound(user, friend);
        double amountPerPerson = amount / HALF;

        user.splitWithFriend(friend.getUsername(), amountPerPerson, amountPerPerson, reason);
        friend.splitWithFriend(user.getUsername(), amountPerPerson, amountPerPerson, reason);

        user.addToPaymentHistory(new SplitHistoryEntry(friendUsername, reason, amount, PaymentType.FRIEND));
        friend.addToPaymentHistory(new SplitHistoryEntry(user.getUsername(), reason, amount, PaymentType.FRIEND));

        sendFriendNotification(friend, new FriendOwedMoney(user.getUsername(), reason, amountPerPerson));
    }

    @Override
    public void splitGroup(String groupName, User user, double amount, String reason)
        throws NotMemberOfGroupException, GroupNotFoundException, NegativeAmountException {
        assertStringIsNotNullOrBlank(groupName, GROUP_NAME);
        assertUserIsNotNull(user);
        assertPositiveAmount(amount);
        assertStringIsNotNullOrBlank(reason, REASON);
        assertGroupIsFound(groupName);
        assertUserIsMemberOfGroup(groupName, user);

        int groupSize = groups.get(groupName).getMembers().size();
        double amountPerPerson = DoubleFormatter.divide(amount, groupSize);
        double owedByTheWholeGroupPerson = amount - amountPerPerson;
        groups.get(groupName).addDueAmount(owedByTheWholeGroupPerson, reason);
        user.splitWithGroup(groupName,amountPerPerson,owedByTheWholeGroupPerson,reason);


        for (User member : groups.get(groupName).getMembers()) {
            if(!member.equals(user)) {
                member.addToPaymentHistory(new SplitHistoryEntry(groupName, reason, amount, PaymentType.GROUP));
                member.splitWithGroup(groupName,amountPerPerson,owedByTheWholeGroupPerson,reason);
            }
        }

        sendGroupNotificationToAllMembers(groupName, amountPerPerson, reason, user);
    }

    @Override
    public String getStatus(User user) {
        return user.getUserStatus();
    }

    @Override
    public void payed(String payingUserUsername, double amount, User user, String reason)
        throws FriendNotFoundException, NegativeAmountException, NoDueAmountFoundException, UserNotFoundException,
        NoDueAmountOwedToException, NoDueAmountOwedByException {
        assertUserIsNotNull(user);
        assertStringIsNotNullOrBlank(payingUserUsername, PAYING_USER_USERNAME);
        assertStringIsNotNullOrBlank(reason, REASON);
        assertPositiveAmount(amount);
        assertUserIsFound(payingUserUsername);
        User payingUser = users.get(payingUserUsername);
        assertFriendIsFound(user, payingUser);

        payingUser.payedDebtTo(amount, user.getUsername(), reason, PaymentType.FRIEND);
        user.payedDebtBy(amount, payingUserUsername , reason, PaymentType.FRIEND);

        sendFriendNotification(payingUser, new FriendApprovedMoney(user.getUsername(), reason, amount));
    }

    @Override
    public void payedGroup(String payingUserUsername, String groupName, User user, double amount, String reason)
        throws NegativeAmountException, GroupNotFoundException, NotMemberOfGroupException, NoDueAmountFoundException,
        UserNotFoundException, NoDueAmountOwedToException, NoDueAmountOwedByException {
        assertUserIsNotNull(user);
        assertStringIsNotNullOrBlank(payingUserUsername, PAYING_USER_USERNAME);
        assertStringIsNotNullOrBlank(reason, REASON);
        assertStringIsNotNullOrBlank(groupName, GROUP_NAME);
        assertUserIsFound(payingUserUsername);
        User payingUser = users.get(payingUserUsername);
        assertPositiveAmount(amount);
        assertGroupIsFound(groupName);
        assertUserIsMemberOfGroup(groupName, user);
        assertPayingUserIsMemberOfGroup(groupName, payingUser);

        payingUser.payedDebtTo(amount, groupName, reason, PaymentType.GROUP);
        user.payedDebtBy(amount,groupName,reason, PaymentType.GROUP);


        sendGroupNotification(payingUser, groupName,
            new GroupApprovedMoney(user.getUsername(), reason, amount, groupName));

        for (User member: groups.get(groupName).getMembers()) {
            if (!member.equals(payingUser) && !member.equals(user)) {
                member.payedDebtBy(amount, groupName, reason, PaymentType.GROUP);
            }
        }
    }

    @Override
    public List<SplitHistoryEntry> getPaymentHistory(User user) {
        assertUserIsNotNull(user);
        return user.getPaymentHistory();
    }

    @Override
    public double getTotalAmount(User user, String target, boolean isGroup, boolean isOwedTo) {
        assertUserIsNotNull(user);
        assertStringIsNotNullOrBlank(target, "Target");

        return user.getTotalAmount(target, isGroup, isOwedTo);
    }
}
