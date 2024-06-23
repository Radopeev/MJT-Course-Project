package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroupException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class SplitGroup extends CommandBase {
    private String groupName;
    private double amount;
    private String reason;
    private static final String MESSAGE = "You have split %s LV with %s for %s" + System.lineSeparator();
    public SplitGroup(UserRepository userRepository, User user, String groupName, double amount, String reason) {
        super(userRepository, user);
        this.groupName = groupName;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public String execute() {
        try {
            userRepository.splitGroup(groupName, user, amount, reason);
        } catch (GroupNotFoundException | NotMemberOfGroupException | NegativeAmountException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, user.getUsername(), e.getStackTrace()));
            return e.getMessage();
        }
        return String.format(MESSAGE, amount, groupName, reason);
    }
}
