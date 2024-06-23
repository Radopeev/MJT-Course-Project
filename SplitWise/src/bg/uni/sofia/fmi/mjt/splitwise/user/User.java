package bg.uni.sofia.fmi.mjt.splitwise.user;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedByException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedToException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.notification.Notification;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.PaymentType;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.SplitHistoryEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class User implements Serializable {
    private List<SplitHistoryEntry> splitHistory;
    private List<Notification> friendsNotifications;
    private Map<String, List<Notification>> groupsNotifications;
    private String username;
    private String password;
    private Map<String, Map<String, Double>> amountOwedToFriends;
    private Map<String, Map<String, Double>> amountOwedByFriends;
    private Map<String, Map<String, Double>> amountOwedToGroups;
    private Map<String, Map<String, Double>> amountOwedByGroups;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.amountOwedToFriends = new LinkedHashMap<>();
        this.amountOwedByFriends = new LinkedHashMap<>();
        this.amountOwedToGroups = new LinkedHashMap<>();
        this.amountOwedByGroups = new LinkedHashMap<>();
        this.friendsNotifications = new ArrayList<>();
        this.groupsNotifications = new LinkedHashMap<>();
        splitHistory = new ArrayList<>();
    }

    public List<SplitHistoryEntry> getSplitHistory() {
        return splitHistory;
    }

    public String seeNotifications() {
        if (friendsNotifications.isEmpty() && groupsNotifications.isEmpty()) {
            return "No notifications to show" + System.lineSeparator();
        }

        StringBuilder notifications = new StringBuilder("Friends: " + System.lineSeparator());
        appendFriendNotifications(notifications);
        notifications.append(System.lineSeparator());
        notifications.append("Groups: ").append(System.lineSeparator());
        appendGroupNotifications(notifications);

        friendsNotifications.clear();
        groupsNotifications.clear();
        return notifications + System.lineSeparator();
    }

    private void appendFriendNotifications(StringBuilder notifications) {
        if (!friendsNotifications.isEmpty()) {
            notifications.append(friendsNotifications.stream()
                .map(Notification::inform)
                .map(notification -> "\t " + notification)
                .collect(Collectors.joining(System.lineSeparator())));
        } else {
            notifications.append("\t No notifications");
        }
    }

    private void appendGroupNotifications(StringBuilder notifications) {
        if (!groupsNotifications.isEmpty()) {
            groupsNotifications.forEach((group, groupNotifications) -> {
                if (groupNotifications.isEmpty()) {
                    notifications.append("\t ").append(group)
                        .append(System.lineSeparator()).append("\t\t You have no notifications in this group");
                } else {
                    appendGroupNotification(notifications, group, groupNotifications);
                    notifications.append(System.lineSeparator());
                }
            });
        } else {
            notifications.append("\t No notifications");
        }
    }

    private void appendGroupNotification(StringBuilder notifications,
                                         String group, List<Notification> groupNotifications) {
        notifications.append("\t ").append(group).append(System.lineSeparator()).append(groupNotifications.stream()
            .map(Notification::inform)
            .map(notification -> "\t\t " + notification)
            .collect(Collectors.joining(System.lineSeparator())));
    }

    public void addFriend(String newFriend) {
        amountOwedByFriends.put(newFriend, new LinkedHashMap<>());
        amountOwedToFriends.put(newFriend, new LinkedHashMap<>());
    }

    public void addGroup(String newGroup) {
        amountOwedByGroups.put(newGroup, new LinkedHashMap<>());
        amountOwedToGroups.put(newGroup, new LinkedHashMap<>());
        groupsNotifications.put(newGroup, new ArrayList<>());
    }

    public Set<String> getFriends() {
        return amountOwedByFriends.keySet();
    }

    public Set<String> getGroups() {
        return amountOwedByGroups.keySet();
    }

    public String getUsername() {
        return username;
    }


    private void splitWith(String target, double amountOwedTo, double amountOwedBy, String reason, PaymentType type) {
        Map<String, Map<String, Double>> owedMoneyTo;
        Map<String, Map<String, Double>> owedBy;

        switch (type) {
            case GROUP -> {
                owedMoneyTo = amountOwedToGroups;
                owedBy = amountOwedByGroups;
            }
            case FRIEND -> {
                owedMoneyTo = amountOwedToFriends;
                owedBy = amountOwedByFriends;
            }
            default -> {
                return;
            }
        }

        if (owedMoneyTo.get(target).containsKey(reason)) {
            owedMoneyTo.get(target).replace(reason, owedMoneyTo.get(target).get(reason) + amountOwedTo);
        } else {
            owedMoneyTo.get(target).put(reason, amountOwedTo);
        }

        if (Double.compare(amountOwedBy, 0.0) != 0) {
            owedBy.get(target).put(reason, amountOwedBy);
        }
    }


    public double getTotalAmount(String target, boolean isGroup, boolean isOwedTo) {
        Map<String, Map<String, Double>> targetMap = isGroup ?
            (isOwedTo ? amountOwedToGroups : amountOwedByGroups) :
            (isOwedTo ? amountOwedToFriends : amountOwedByFriends);

        if (targetMap.containsKey(target)) {
            Map<String, Double> debts = targetMap.get(target);
            return debts.values().stream().mapToDouble(Double::doubleValue).sum();
        }
        return 0.0;
    }

    public void splitWithFriend(String friend, double amountOwedTo, double amountOwedBy, String reason) {
        splitWith(friend, amountOwedTo, amountOwedBy, reason, PaymentType.FRIEND);
    }

    public void splitWithGroup(String group, double amountOwedTo, double amountOwedBy, String reason) {
        splitWith(group, amountOwedTo, amountOwedBy, reason, PaymentType.GROUP);
    }

    public void payedDebtTo(double amount, String friendOrGroupName, String reason, PaymentType type)
        throws NoDueAmountOwedToException {
        try {
            payedDebt(amount, friendOrGroupName, reason, type,true);
        } catch (NoDueAmountFoundException e){
            ExceptionSaver.saveException(new SplitWiseException(e, username, e.getStackTrace()));
            throw new NoDueAmountOwedToException("You don't owe money to " + friendOrGroupName);
        }
    }

    public void payedDebtBy(double amount, String friendOrGroupName, String reason, PaymentType type)
        throws NoDueAmountOwedByException {
        try {
            payedDebt(amount, friendOrGroupName, reason, type,false);
        } catch (NoDueAmountFoundException e){
            ExceptionSaver.saveException(new SplitWiseException(e, username, e.getStackTrace()));
            throw new NoDueAmountOwedByException("You aren't owed money from " + friendOrGroupName);
        }

    }

    private void payedDebt(double amount, String friendOrGroupName, String reason, PaymentType type, boolean isOwedTo) throws NoDueAmountFoundException {
        Map<String, Map<String, Double>> targetMap;
        switch (type){
            case GROUP -> targetMap = isOwedTo ? amountOwedToGroups : amountOwedByGroups;
            case FRIEND -> targetMap = isOwedTo ? amountOwedToFriends : amountOwedByFriends;
            default -> {
                return;
            }
        }
        if (targetMap.get(friendOrGroupName).containsKey(reason)) {
            double newValue = targetMap.get(friendOrGroupName).get(reason) - amount;
            if (newValue <= 0.0) {
                targetMap.get(friendOrGroupName).remove(reason);
            } else {
                targetMap.get(friendOrGroupName).put(reason, newValue);
            }
            return;
        }
        throw new NoDueAmountFoundException("No due amount found for " + reason);
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void addFriendNotification(Notification notification) {
        friendsNotifications.add(notification);
    }

    public void addGroupNotification(String groupName, Notification notification) {
        groupsNotifications.get(groupName).add(notification);
    }

    public List<Notification> getFriendsNotifications() {
        return friendsNotifications;
    }

    public Map<String, List<Notification>> getGroupsNotifications() {
        return groupsNotifications;
    }

    public List<SplitHistoryEntry> getPaymentHistory() {
        return splitHistory;
    }

    public void addToPaymentHistory(SplitHistoryEntry payment) {
        splitHistory.add(payment);
    }

    public Map<String, Double> getOwedByFriendDueAmounts(String friend) {
        return amountOwedByFriends.get(friend);
    }

    public Map<String, Double> getOwedToFriendDueAmounts(String friend) {
        return amountOwedToFriends.get(friend);
    }

    public Map<String, Double> getOwedByGroupDueAmounts(String groupName) {
        return amountOwedByGroups.get(groupName);
    }

    public Map<String, Double> getOwedToGroupDueAmounts(String groupName) {
        return amountOwedToGroups.get(groupName);
    }

    public String getUserStatus() {
        StringBuilder status = new StringBuilder("User Status for " + username + ":" + System.lineSeparator());

        appendFriendsSection(status);
        appendGroupSection(status);

        return status.toString();
    }

    private void appendFriendsSection(StringBuilder status) {
        status.append("Owed to Friends:").append(System.lineSeparator());
        if (amountOwedToFriends.isEmpty() || amountOwedToFriends.values().stream().allMatch(Map::isEmpty)) {
            status.append("\t You don't owe money to friends").append(System.lineSeparator());
        } else {
            appendStatusDetails(status, amountOwedToFriends);
        }

        status.append(System.lineSeparator());
        status.append("Owed by Friends:").append(System.lineSeparator());
        if (amountOwedByFriends.isEmpty() || amountOwedByFriends.values().stream().allMatch(Map::isEmpty)) {
            status.append("\t You aren't owed money by friends").append(System.lineSeparator());
        } else {
            appendStatusDetails(status, amountOwedByFriends);
        }
    }

    private void appendGroupSection(StringBuilder status) {
        status.append(System.lineSeparator());
        status.append("Owed to Groups:").append(System.lineSeparator());
        if (amountOwedToGroups.isEmpty()) {
            status.append("\t You don't owe money in any groups").append(System.lineSeparator());
        } else {
            appendGroupStatusDetails(status, amountOwedToGroups);
        }

        status.append(System.lineSeparator());
        status.append("Owed by Groups:").append(System.lineSeparator());
        if (amountOwedByGroups.isEmpty()) {
            status.append("\t You are no owed money in any groups").append(System.lineSeparator());
        } else {
            appendGroupStatusDetails(status, amountOwedByGroups);
        }
    }

    private void appendStatusDetails(StringBuilder status, Map<String, Map<String, Double>> targetMap) {
        targetMap.forEach((target, debts) -> {
            if (!debts.isEmpty()) {
                status.append("\t ").append(target).append(":").append(System.lineSeparator());
                debts.forEach((reason, amount) ->
                    status.append("\t\t ").append("Reason: ").append(reason)
                        .append(", Amount: ")
                        .append(String.format("%.2f", amount)).append(System.lineSeparator())
                );
            }
        });
    }

    private void appendGroupStatusDetails(StringBuilder status, Map<String, Map<String, Double>> targetMap) {
        targetMap.forEach((group, debts) -> {
            if (!debts.isEmpty()) {
                status.append("\t ").append(group).append(":").append(System.lineSeparator());
                debts.forEach((reason, amount) ->
                    status.append("\t\t ").append("Reason: ").append(reason)
                        .append(", Amount: ")
                        .append(String.format("%.2f", amount)).append(System.lineSeparator())
                );
            } else {
                status.append("\t ").append(group).append(": No financial activity in this group")
                    .append(System.lineSeparator());
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
