package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class AddFriend extends CommandBase {
    private String friendUsername;
    private static final String MESSAGE = "You have successfully added %s as your friend" + System.lineSeparator();
    public AddFriend(UserRepository userRepository, User user, String friendUsername) {
        super(userRepository, user);
        this.friendUsername = friendUsername;
    }

    @Override
    public String execute() {
        try {
            userRepository.addFriendToUser(user, friendUsername);
        } catch (UserNotFoundException | AlreadyFriendsException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, user.getUsername(), e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return String.format(MESSAGE, friendUsername);
    }
}
