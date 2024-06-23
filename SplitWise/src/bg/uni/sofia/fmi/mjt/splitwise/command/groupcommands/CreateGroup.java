package bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

import java.util.Set;

public class CreateGroup extends CommandBase {

    private Set<String> members;
    private String groupName;
    private static final String MESSAGE = "You have successfully created %s group" + System.lineSeparator();
    public CreateGroup(UserRepository userRepository, User user, Set<String> members, String groupName) {
        super(userRepository, user);
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public String execute() {
        try {
            userRepository.createGroup(members, groupName);
        } catch (GroupAlreadyExistsException | UserNotFoundException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, user.getUsername(), e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return String.format(MESSAGE, groupName);
    }
}
