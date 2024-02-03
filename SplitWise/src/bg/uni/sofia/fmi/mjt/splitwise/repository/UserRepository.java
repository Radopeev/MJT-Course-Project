package bg.uni.sofia.fmi.mjt.splitwise.repository;

import bg.uni.sofia.fmi.mjt.splitwise.database.DatabaseReader;
import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriends;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExists;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroup;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTaken;
import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.notification.ApprovedMoneyNotification;
import bg.uni.sofia.fmi.mjt.splitwise.notification.OwedMoneyNotification;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class UserRepository {
    private final Map<String, User> users;
    private final Map<String, Group> groups;

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

    public User getUser(String username){
        return users.get(username);
    }
    public void addUser(User user) throws UsernameAlreadyTaken {
        if (users.containsKey(user.getUsername())){
            throw new UsernameAlreadyTaken("User with this username already exists");
        }
        users.put(user.getUsername(),user);
    }
    public void findUser(User user) throws UserNotFound {
        if(!users.containsValue(user)){
            throw new UserNotFound("User with this username and password not found");
        }
    }
    public void addFriendToUser(User user, String friend) throws UserNotFound, AlreadyFriends {
        if(!users.containsKey(friend)){
            throw new UserNotFound("User with this username not found");
        }
        if(users.get(user.getUsername()).getFriends().containsKey(friend)){
            throw new AlreadyFriends("You are already friends with"+friend);
        }
        user.addFriend(friend);
        users.get(friend).addFriend(user.getUsername());
    }

    public void createGroup(Set<User> members, String groupName) throws GroupAlreadyExists {
        if(groups.containsKey(groupName)){
            throw new GroupAlreadyExists("Group with this name already exists");
        }
        groups.put(groupName, new Group(members,groupName));
    }
    public void split(User userToSplitWith, User user,double amount,String reason) throws FriendNotFound, UserNotFound {
        if(!users.containsKey(userToSplitWith.getUsername())){
            throw new UserNotFound("User with this username not found");
        }
        if(!user.getFriends().containsKey(userToSplitWith.getUsername())){
            throw new FriendNotFound("You are not friend with"+userToSplitWith.getUsername());
        }
        user.splitWithFriend(userToSplitWith.getUsername(),amount/2,reason);
        userToSplitWith.splitWithFriend(user.getUsername(),amount/2,reason);
        userToSplitWith.addFriendNotification(new OwedMoneyNotification(user.getUsername(),new DueAmount(amount/2,reason)));
    }

    public void splitGroup(String groupName,User user, double amount, String reason)
        throws NotMemberOfGroup, GroupNotFound {
        int groupSize = groups.get(groupName).getMembers().size();
        double amountPerEach = amount / groupSize;
        if(!groups.containsKey(groupName)){
            throw new GroupNotFound(groupName + " does not exists");
        }
        if (!groups.get(groupName).containsUser(user)) {
            throw new NotMemberOfGroup("You are not a member of this group");
        }
        for(User curr: groups.get(groupName).getMembers()){
            curr.splitWithGroup(groupName,amountPerEach,reason);
            if(!curr.equals(user)){
                curr.addGroupNotification(groupName, new OwedMoneyNotification(user.getUsername(),new DueAmount(amount,reason)));
            }
        }
        groups.get(groupName).updateDueAmount(amountPerEach,reason);
    }
    public String getStatus(User user){
        StringBuilder status = new StringBuilder();
        status.append("Friends: ").append(System.lineSeparator());
        for(Map.Entry<String, DueAmount> entry: user.getFriends().entrySet()){
            if(entry.getValue()!=null) {
                status.append("\t* ").append(entry.getKey()).
                    append(": You owe ").append(entry.getValue().getAmount()).append(System.lineSeparator());
            }
        }
        for(Map.Entry<String, User> entry: users.entrySet()){
            if(entry.getValue().getFriends().containsKey(user.getUsername())
                && entry.getValue().getFriends().get(user.getUsername())!=null) {
                status.append("\t* ").append(entry.getKey())
                    .append(": Owes you ").append(entry.getValue().getFriends().get(user.getUsername()).getAmount()).append(System.lineSeparator());
            }
        }
        status.append("Groups:\n");
        if(groups.values().stream().noneMatch(group -> group.containsUser(user))){
            return status.append("\tYou are not in any groups").toString();
        }
        for(Map.Entry<String, DueAmount> entry: user.getGroups().entrySet()){
            if(entry.getValue()!=null) {
                status.append("\t* ").append(entry.getKey()).
                    append(": You owe ").append(entry.getValue().getAmount()).append(System.lineSeparator());
            }
        }
        for(Map.Entry<String, Group> entry: groups.entrySet()){
            if(entry.getValue().getMembers().contains(user)) {
                status.append("\t*").append(entry.getKey())
                    .append(" : Owes you ").append(entry.getValue().getDueAmounts().get(user).getAmount()).append(System.lineSeparator());
            }
        }
        return status.toString();
    }
    public void payed(User payingUser,double amount, User user) throws FriendNotFound {
        if(!user.getFriends().containsKey(payingUser.getUsername())){
            throw new FriendNotFound("You are not friends with" + payingUser.getUsername());
        }
        payingUser.payedDebt(amount,user.getUsername());
        payingUser.addFriendNotification(new ApprovedMoneyNotification(user.getUsername(),
            users.get(payingUser.getUsername()).getFriends().get(user.getUsername())));
    }
}
