package bg.uni.sofia.fmi.mjt.splitwise.repository;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;
import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class UserRepository {
    private final Map<String, User> users;
    private final Map<String, Group> groups;

    public UserRepository(Path usersFile, Path groupsFile) throws IOException {
        this.users = new HashMap<>();
        this.groups = new HashMap<>();

        if(Files.size(usersFile)> 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile.toFile()))) {
                while (true) {
                    try {
                        Object userObject = ois.readObject();
                        if (userObject instanceof User user) {
                            users.put(user.getUsername(), user);
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if(Files.size(groupsFile) > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(groupsFile.toFile()))) {
                while (true) {
                    try {
                        Object groupObject = ois.readObject();
                        if (groupObject instanceof Group group) {
                            groups.put(group.getGroupName(), group);
                        }
                    } catch (EOFException e) {
                        // End of file reached, exit the loop
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public User getUser(String username){
        return users.get(username);
    }
    public void addUser(User user){
        users.put(user.getUsername(),user);
    }
    public boolean contains(User user){
        return users.containsValue(user);
    }
    public void addFriendToUser(User user, String friend) {
        if (user != null) {
            user.addFriend(friend);
        }
    }

    public void createGroup(Set<User> members, String groupName){
        groups.put(groupName, new Group(members,groupName));
    }
    public void split(User userToSplitWith, User user,double amount,String reason){
        user.splitWithFriend(userToSplitWith.getUsername(),amount/2,reason);
        userToSplitWith.splitWithFriend(user.getUsername(),amount/2,reason);
    }

    public void splitGroup(String groupName,User user, double amount, String reason){
        int groupSize = groups.get(groupName).getMembers().size();
        double amountPerEach = amount / groupSize;
        if (!groups.get(groupName).containsUser(user)) {
            // TODD : throw an exception
        }
        for(User curr: groups.get(groupName).getMembers()){
            curr.splitWithGroup(groupName,amountPerEach,reason);
        }
        groups.get(groupName).updateDueAmount(amountPerEach,reason);
    }
    public String getStatus(User user){
        StringBuilder status = new StringBuilder();
        status.append("Friends:\n");
        for(Map.Entry<String, Optional<DueAmount>> entry: user.getFriends().entrySet()){
            if(entry.getValue().isPresent()) {
                status.append("*").append(entry.getKey()).
                    append(": You owe ").append(entry.getValue().get().getAmount()).append("\n");
            }
        }
        for(Map.Entry<String, User> entry: users.entrySet()){
            if(entry.getValue().getFriends().containsKey(user.getUsername())
                && entry.getValue().getFriends().get(user.getUsername()).isPresent()) {
                status.append("*").append(entry.getKey())
                    .append(": Owes you ").append(entry.getValue().getFriends().get(user.getUsername()).get()).append("\n");
            }
        }
        status.append("Groups:\n");
        for(Map.Entry<String, Optional<DueAmount>> entry: user.getGroups().entrySet()){
            if(entry.getValue().isPresent()) {
                status.append("*").append(entry.getKey()).
                    append(": You owe ").append(entry.getValue().get().getAmount());
            }
        }
        for(Map.Entry<String, Group> entry: groups.entrySet()){
            if(entry.getValue().getMembers().contains(user)) {
                status.append("*").append(entry.getKey())
                    .append(": Owes you ").append(entry.getValue().getDueAmounts().get(user).getAmount()).append("\n");
            }
        }
        return status.toString();
    }
    public void payed(User payingUser,double amount, User user){
        payingUser.payedDebt(amount,user.getUsername());
    }

    public void writeDataToDatabase(Path usersFile,Path groupsFile){
        writeUsersToFile(usersFile);
        writeGroupsToFile(groupsFile);
    }
    private void writeUsersToFile(Path usersFile){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile.toFile()))) {
            for (User user : users.values()) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing users to file", e);
        }
    }

    private void writeGroupsToFile(Path groupsFile){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(groupsFile.toFile()))) {
            for (Group group : groups.values()) {
                oos.writeObject(group);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing groups to file", e);
        }
    }
}
