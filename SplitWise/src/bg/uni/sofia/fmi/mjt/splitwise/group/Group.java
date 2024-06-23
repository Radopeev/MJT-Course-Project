package bg.uni.sofia.fmi.mjt.splitwise.group;

import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Group implements Serializable {
    private String groupName;
    private Set<User> members;
    private Map<User, Map<String, Double>> dueAmounts;

    public Group(Set<User> members, String groupName) {
        this.members = members;
        this.groupName = groupName;
        this.dueAmounts = new HashMap<>();
        for (User curr : members) {
            dueAmounts.put(curr, new HashMap<>());
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean containsUser(User user) {
        return members.contains(user);
    }

    public Set<User> getMembers() {
        return members;
    }

    public Map<User, Map<String, Double>> getDueAmounts() {
        return dueAmounts;
    }

    public void addDueAmount(double amount, String reason) {
        dueAmounts.forEach((key, value) -> {
            if (value.containsKey(reason)) {
                value.replace(reason, value.get(reason) + amount);
            } else {
                value.put(reason, amount);
            }
        });
    }
}
