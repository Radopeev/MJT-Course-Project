package bg.uni.sofia.fmi.mjt.splitwise.group;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Group implements Serializable {
    private String groupName;
    private Set<User> members;
    private Map<User, DueAmount> dueAmounts;

    public Group(Set<User> members, String groupName){
        this.members = members;
        this.groupName = groupName;
        this.dueAmounts = new HashMap<>();
    }
    public String getGroupName(){
        return groupName;
    }
    public boolean containsUser(User user){
        return members.contains(user);
    }
    public Set<User> getMembers(){
        return members;
    }
    public Map<User, DueAmount> getDueAmounts(){
        return dueAmounts;
    }

    public void updateDueAmount(double amount,String reason){
        dueAmounts.replaceAll((u, v) -> new DueAmount(amount, reason));
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Group group = (Group) obj;
        return Objects.equals(groupName, group.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, members, dueAmounts);
    }
}
