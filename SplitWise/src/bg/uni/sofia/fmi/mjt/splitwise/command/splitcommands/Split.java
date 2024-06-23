package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Split extends CommandBase {

    private String friendUsername;
    private double amount;
    private String reason;
    private static final String MESSAGE = "You have split %s LV with %s for %s" + System.lineSeparator();
    public Split(UserRepository userRepository, User user, String friendUsername, double amount, String reason) {
        super(userRepository, user);
        this.friendUsername = friendUsername;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public String execute() {
        try {
            userRepository.split(friendUsername, user, amount, reason);
        } catch (FriendNotFoundException | NegativeAmountException | UserNotFoundException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, user.getUsername(), e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return String.format(MESSAGE, amount , friendUsername, reason);
    }
}
