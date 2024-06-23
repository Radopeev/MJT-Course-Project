package bg.uni.sofia.fmi.mjt.splitwise.database;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.group.Group;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class DatabaseSaver {
    private static void writeUsersToFile(Path usersFile, UserRepository userRepository) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile.toFile()))) {
            for (User user : userRepository.getUsers().values()) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, "No user", e.getStackTrace()));
            throw new RuntimeException("Error writing users to file", e);
        }
    }

    private static void writeGroupsToFile(Path groupsFile, UserRepository userRepository) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(groupsFile.toFile()))) {
            for (Group group : userRepository.getGroups().values()) {
                oos.writeObject(group);
            }
        } catch (IOException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, "No user", e.getStackTrace()));
            throw new RuntimeException("Error writing groups to file", e);
        }
    }

    public static void writeDataToDatabase(Path usersFile, Path groupsFile, UserRepository userRepository) {
        writeUsersToFile(usersFile, userRepository);
        writeGroupsToFile(groupsFile, userRepository);
    }
}

