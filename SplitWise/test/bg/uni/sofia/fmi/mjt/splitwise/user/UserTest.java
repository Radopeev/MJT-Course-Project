package bg.uni.sofia.fmi.mjt.splitwise.user;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.notification.FriendApprovedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.FriendOwedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.GroupApprovedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.GroupOwedMoney;
import bg.uni.sofia.fmi.mjt.splitwise.notification.Notification;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void testSeeNotificationsWhenThereNoNotifications() {
        User user = new User("rado","rado");
        String expected = "No notifications to show" + System.lineSeparator();

        assertEquals(expected, user.seeNotifications());
    }

    @Test
    void testSeeNotificationsWhenThereFriendsNotificationsButNoGroup() {
        User user = new User("rado","rado");
        user.addFriendNotification(new FriendApprovedMoney("dobo","lemons",5.0));
        user.addFriendNotification(new FriendOwedMoney("gosho","oranges",10));
        String expected = "Friends: " + System.lineSeparator()
            + "\t dobo approved your payment 5.0 LV [lemons]" + System.lineSeparator()
            + "\t You owe gosho 10.0 LV [oranges]" + System.lineSeparator()
            + "Groups: " + System.lineSeparator()
            + "\t No notifications" + System.lineSeparator();

        assertEquals(expected, user.seeNotifications(),"Notifications should be: " + expected);
    }

    @Test
    void testSeeNotificationsWhenThereGroupNotificationsButNoFriends() {
        User rado = new User("rado","rado");
        String members = "dobo,gosho";
        String group1 = "group1";
        rado.addGroup(group1);
        rado.splitWithGroup(group1,5.0,5.0,"lemons");
        rado.addGroupNotification(group1, new GroupApprovedMoney("dobo", "lemons",2.5,group1));
        rado.addGroupNotification("group1", new GroupOwedMoney("oranges",3, group1, members));
        String expected = "Friends: " + System.lineSeparator()
            + "\t No notifications" + System.lineSeparator()
            + "Groups: " + System.lineSeparator()
            + "\t group1" + System.lineSeparator()
            + "\t\t Your payment of 2.5 in group1 [lemons] has been approved by dobo" + System.lineSeparator()
            + "\t\t You owe dobo,gosho 3.0 LV [oranges] in group1" + System.lineSeparator() + System.lineSeparator();
        String result = rado.seeNotifications();

        assertEquals(expected, result,"Notifications should be: " + expected);
    }

    @Test
    void testSeeNotifications(){
        User user = new User("rado","rado");
        String members = "dobo,gosho";
        String group1 = "group1";
        String group2 = "group2";
        user.addGroup(group1);
        user.addGroup(group2);
        user.splitWithGroup(group1,5.0,5.0,"lemons");
        user.addFriendNotification(new FriendApprovedMoney("dobo","lemons",5));
        user.addFriendNotification(new FriendOwedMoney("gosho", "oranges",10));
        user.addGroupNotification(group1, new GroupApprovedMoney("dobo",
            "lemons",2.5,"group1"));
        user.addGroupNotification(group1, new GroupOwedMoney(
            "oranges",3, "group1", members));
        String expected = "Friends: " + System.lineSeparator()
            + "\t dobo approved your payment 5.0 LV [lemons]" + System.lineSeparator()
            + "\t You owe gosho 10.0 LV [oranges]" + System.lineSeparator()
            + "Groups: " + System.lineSeparator()
            + "\t group1" + System.lineSeparator()
            + "\t\t Your payment of 2.5 in group1 [lemons] has been approved by dobo" + System.lineSeparator()
            + "\t\t You owe dobo,gosho 3.0 LV [oranges] in group1" + System.lineSeparator()
            + "\t group2" + System.lineSeparator()
            + "\t\t You have no notifications in this group" + System.lineSeparator();

        assertEquals(expected, user.seeNotifications(), "Notifications should be : " + expected);
    }

    @Test
    void testAddFriend(){
        User rado = new User("rado","rado");
        String newFriend = "dobo";
        rado.addFriend(newFriend);

        assertTrue(rado.getFriends().contains("dobo"),"User should contain new dobo as key");
    }

    @Test
    void testAddGroup(){
        User rado = new User("rado","rado");
        String group = "group";
        rado.addGroup(group);

        assertTrue(rado.getGroups().contains(group),"User should contain new group as key");
    }

    @Test
    void testSplitWithFriend() {
        User rado = new User("rado", "rado");
        String dobo = "dobo";
        rado.addFriend(dobo);
        rado.splitWithFriend(dobo,5.0,5.0,"lemons");
        rado.splitWithFriend(dobo, 10.0,10.0,"oranges");
        Map<String, Double> expected = Map.of(
            "lemons", 5.0,
            "oranges", 10.0
        );

        assertEquals(expected, rado.getOwedByFriendDueAmounts(dobo),"Initial splits should match");
        assertEquals(expected, rado.getOwedToFriendDueAmounts(dobo),"Initial splits should match");

        rado.splitWithFriend(dobo,10,10,"lemons");
        assertEquals(15, rado.getOwedToFriendDueAmounts(dobo).get("lemons"),"Updated split should match");
    }

    @Test
    void testSplitWithGroup(){
        User rado = new User("rado", "rado");
        String group = "group";
        rado.addGroup(group);
        rado.splitWithGroup(group,5.0,5.0,"lemons");
        rado.splitWithGroup(group, 10.0,10.0,"oranges");
        Map<String, Double> expected = Map.of(
            "lemons", 5.0,
            "oranges", 10.0
        );

        assertEquals(expected, rado.getOwedByGroupDueAmounts(group),"Initial splits within the group should match");
        assertEquals(expected, rado.getOwedToGroupDueAmounts(group),"Initial splits within the group should match");
    }

    @Test
    void testPayedDebtWhenIsPayedOff() throws NoDueAmountFoundException {
        User rado = new User("rado","rado");
        String dobo = "dobo";
        rado.addFriend(dobo);
        rado.splitWithFriend(dobo, 2.5, 2.5,"lemons");
        rado.payedDebtBy(3.0,dobo,"lemons",false);

        assertTrue(rado.getOwedByFriendDueAmounts(dobo).isEmpty(),"Debt should be cleared after payment");
    }

    @Test
    void testPayedDebtWhenDebtIsFromFriend() throws NoDueAmountFoundException {
        User rado = new User("rado","rado");
        String dobo = "dobo";
        rado.addFriend(dobo);
        rado.splitWithFriend(dobo, 5.0, 5.0,"lemons");
        rado.payedDebtBy(3.0,dobo,"lemons",false);
        double actual = rado.getOwedByFriendDueAmounts(dobo).get("lemons");

        assertEquals(2.0,actual,"Remaining debt after partial payment should be 2.0");
    }

    @Test
    void testPayedDebtWhenDebtIsFromGroup() throws NoDueAmountFoundException {
        User rado = new User("rado","rado");
        String group = "group";
        rado.addGroup(group);
        rado.splitWithGroup(group, 5.0, 5.0,"lemons");
        rado.payedDebtBy(2.5,group,"lemons",true);
        double actual = rado.getOwedByGroupDueAmounts(group).get("lemons");

        assertEquals(2.5, actual,"Remaining group debt after partial payment should be 2.5");
    }

    @Test
    void testPayedDebtWhenReasonDoesNotExist() {
        User rado = new User("rado","rado");
        String group = "group";
        rado.addGroup(group);
        rado.splitWithGroup(group, 5.0, 5.0,"lemons");

        assertThrows(NoDueAmountFoundException.class, () -> rado.payedDebtBy(1.0,group,"oranges",true),
            "Exception should be thrown when trying to pay off debt for a non-existent reason");
    }

    @Test
    void testIsPasswordCorrect(){
        User rado = new User("rado","rado");

        assertTrue(rado.isPasswordCorrect("rado"),"Password 'rado' should be correct");
        assertFalse(rado.isPasswordCorrect("dobo"),"Password 'dobo' should be incorrect");
    }

    @Test
    void testAddFriendNotification(){
        User rado = new User("rado","rado");
        Notification n1 = new FriendApprovedMoney("dobo","lemons",5.0);
        Notification n2 = new FriendOwedMoney("gosho", "oranges",10.0);
        rado.addFriendNotification(n1);
        rado.addFriendNotification(n2);
        List<Notification> expected = List.of(n1,n2);

        assertIterableEquals(expected, rado.getFriendsNotifications(),"Friend notifications should be added correctly");
    }

    @Test
    void testAddGroupNotification(){
        User rado = new User("rado","rado");
        String members = "dobo,gosho";
        String group1 = "group1";
        rado.addGroup(group1);
        rado.splitWithGroup(group1,5.0,5.0,"lemons");
        Notification n1 = new GroupApprovedMoney("dobo","lemons",2.5,group1);
        Notification n2 = new GroupOwedMoney("oranges",3.0, group1, members);
        rado.addGroupNotification(group1, n1);
        rado.addGroupNotification("group1",n2 );
        List<Notification> expected = List.of(n1,n2);

        assertTrue(rado.getGroupsNotifications().containsKey("group1"),"Group notifications map should contain 'group1'");
        assertIterableEquals(expected, rado.getGroupsNotifications().get("group1"),"Group notifications should be added correctly");
    }

    @Test
    public void testGetUserStatus() throws NoDueAmountFoundException {
        String actualStatus = setupStatus();

        String expectedStatus = "User Status for testUser:" + System.lineSeparator()
            + "Owed to Friends:" + System.lineSeparator()
            + "\t friend1:" + System.lineSeparator()
            + "\t\t Reason: Reason1, Amount: 5.0" + System.lineSeparator()
            + "\t friend2:" + System.lineSeparator()
            + "\t\t Reason: Reason2, Amount: 5.0" + System.lineSeparator()
            + "\t\t Reason: Reason3, Amount: 3.0" + System.lineSeparator()
            + System.lineSeparator()
            + "Owed by Friends:" + System.lineSeparator()
            + "\t friend1:" + System.lineSeparator()
            + "\t\t Reason: Reason1, Amount: 10.0" + System.lineSeparator()
            + System.lineSeparator()
            + "Owed to Groups:" + System.lineSeparator()
            + "\t group1:" + System.lineSeparator()
            + "\t\t Reason: Reason4, Amount: 5.0" + System.lineSeparator()
            + System.lineSeparator()
            + "Owed by Groups:" + System.lineSeparator()
            + "\t group1: No financial activity in this group" + System.lineSeparator();

        assertEquals(expectedStatus, actualStatus, "Result should be " + expectedStatus);
    }

    private static String setupStatus() throws NoDueAmountFoundException {
        User user = new User("testUser", "password");
        user.addFriend("friend1");
        user.addFriend("friend2");
        user.addGroup("group1");

        user.splitWithFriend("friend1", 10.0, 10, "Reason1");
        user.payedDebtTo(5.0, "friend1", "Reason1", false);

        user.splitWithFriend("friend2", 5.0, 0, "Reason2");
        user.splitWithFriend("friend2", 3.0, 0, "Reason3");

        user.splitWithGroup("group1", 20.0, 0.0, "Reason4");
        user.payedDebtTo(15.0, "group1", "Reason4", true);

        return user.getUserStatus();
    }

    @Test
    public void testGetTotalAmount() {
        User user = new User("testUser", "testPassword");
        String friend = "friend1";
        String group = "group1";
        double amountOwedTo = 50.0;
        double amountOwedBy = 30.0;
        String reason = "TestReason";

        user.addFriend(friend);
        user.addGroup(group);
        user.splitWithFriend(friend, amountOwedTo, amountOwedBy, reason);
        user.splitWithGroup(group, amountOwedTo, amountOwedBy, reason);

        double totalOwedToFriend = user.getTotalAmount(friend, false, true);
        double totalOwedByFriend = user.getTotalAmount(friend, false, false);
        double totalOwedToGroup = user.getTotalAmount(group, true, true);
        double totalOwedByGroup = user.getTotalAmount(group, true, false);
        double targetDoesNotExits = user.getTotalAmount("nonexisting",true,false);

        assertEquals(amountOwedTo, totalOwedToFriend, 0.001, "Incorrect total owed to friend");
        assertEquals(amountOwedBy, totalOwedByFriend, 0.001, "Incorrect total owed by friend");
        assertEquals(amountOwedTo, totalOwedToGroup, 0.001, "Incorrect total owed to group");
        assertEquals(amountOwedBy, totalOwedByGroup, 0.001, "Incorrect total owed by group");
        assertEquals(0.0, targetDoesNotExits, "Amount should be zero when target is not present");

    }
}
