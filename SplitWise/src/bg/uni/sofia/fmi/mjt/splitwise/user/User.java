package bg.uni.sofia.fmi.mjt.splitwise.user;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class User implements Serializable {
    private String username;
    private String password;
    private Map<String, Optional<DueAmount>> friends;
    private Map<String,Optional<DueAmount>> groups;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.friends = new HashMap<>();
        this.groups = new HashMap<>();
    }

    public void addFriend(String newFriend) {
        friends.put(newFriend, Optional.empty());
    }

    public Map<String, Optional<DueAmount>> getFriends() {
        return friends;
    }
    public Map<String, Optional<DueAmount>> getGroups(){
        return groups;
    }

    public String getUsername() {
        return username;
    }

    public void splitWithFriend(String friend, double amount, String reason) {
        if (friends.get(friend).isPresent()) {
            friends.get(friend).get().updateAmount(amount);
            return;
        }
        friends.put(friend, Optional.of(new DueAmount(amount, reason)));
    }
    public void splitWithGroup(String group, double amount, String reason) {
        groups.put(group, Optional.of(new DueAmount(amount, reason)));
    }
    public void payedDebt(double amount, String friend){
        friends.get(friend).get().updateAmount(amount);
    }
}
