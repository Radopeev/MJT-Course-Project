package bg.uni.sofia.fmi.mjt.splitwise.database;

import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DatabaseReader {

    public static Map<String, User> readUsers(Path usersFile) throws IOException {
        Map<String,User> users = new HashMap<>();
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
        return users;
    }

    public static Map<String, Group> readGroups(Path groupsFile) throws IOException {
        Map<String, Group> groups = new HashMap<>();
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
        return groups;
    }

}
