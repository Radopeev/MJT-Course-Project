package bg.uni.sofia.fmi.mjt.splitwise.user;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;
import bg.uni.sofia.fmi.mjt.splitwise.notification.ApprovedMoneyNotification;
import bg.uni.sofia.fmi.mjt.splitwise.notification.Notification;
import bg.uni.sofia.fmi.mjt.splitwise.notification.OwedMoneyNotification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class User implements Serializable {
    private List<Notification> friendsNotifications;
    private Map<String,List<Notification>> groupsNotifications;
    private String username;
    private String password;
    private Map<String, DueAmount> friends;
    private Map<String,DueAmount> groups;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.friends = new HashMap<>();
        this.groups = new HashMap<>();
        this.friendsNotifications = new ArrayList<>();
        this.groupsNotifications = new HashMap<>();
    }
    public String seeNotifications() {
        if(friendsNotifications.isEmpty() && groupsNotifications.isEmpty()){
            return "No notifications to show";
        }
        StringBuilder notifications = new StringBuilder("Friends" + System.lineSeparator());
        if(!friendsNotifications.isEmpty()) {
            notifications.append(friendsNotifications.stream()
                .map(Notification::inform)
                .collect(Collectors.joining(System.lineSeparator())));
        } else {
            notifications.append("No notifications");
        }
        notifications.append(System.lineSeparator());
        notifications.append("Groups").append(System.lineSeparator());
        if(!groupsNotifications.isEmpty()) {
            groupsNotifications.forEach((group, groupNotifications) -> {
                notifications.append(group).append(":").append(System.lineSeparator());
                notifications.append(groupNotifications.stream()
                    .map(Notification::inform)
                    .collect(Collectors.joining(System.lineSeparator())));
                notifications.append(System.lineSeparator());
            });
        } else {
            notifications.append("No notifications");
        }
        friendsNotifications.clear();
        groupsNotifications.clear();
        return notifications.toString();
    }

    public void addFriend(String newFriend) {
        friends.put(newFriend, null);
    }

    public Map<String, DueAmount> getFriends() {
        return friends;
    }
    public Map<String, DueAmount> getGroups(){
        return groups;
    }

    public String getUsername() {
        return username;
    }

    public void splitWithFriend(String friend, double amount, String reason) {
        if (friends.get(friend) != null) {
            friends.get(friend).updateAmount(amount);
            return;
        }
        friends.put(friend, new DueAmount(amount, reason));
    }
    public void splitWithGroup(String group, double amount, String reason) {
        groups.put(group, new DueAmount(amount, reason));
    }
    public void payedDebt(double amount, String friend) {
        friends.get(friend).updateAmount(amount);
    }

    public void addFriendNotification(Notification notification){
        friendsNotifications.add(notification);
    }
    public void addGroupNotification(String groupName, Notification notification){
        groupsNotifications.get(groupName).add(notification);
    }
    private void addApprovedMoneyNotification(String friend, double amount, String reason){
        friendsNotifications.add(new OwedMoneyNotification(friend, new DueAmount(amount,reason)));
    }

    private void addOwedMoneyNotification(String friend, double amount, String reason){
        friendsNotifications.add(new ApprovedMoneyNotification(friend, new DueAmount(amount,reason)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return Objects.equals(username, user.username) &&
            Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, friends, groups);
    }


}
