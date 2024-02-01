package bg.uni.sofia.fmi.mjt.splitwise.group;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
        for(User user : dueAmounts.keySet()){
            dueAmounts.put(user,new DueAmount(amount,reason));
        }
    }
}
