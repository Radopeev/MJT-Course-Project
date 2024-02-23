package bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedByException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedToException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroupException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

public class PayedGroup extends CommandBase {
    private String groupName;
    private String payingUser;
    private double amount;
    private String reason;
    private static final String MESSAGE = "%s has payed %s LV [%s] in %s" + System.lineSeparator();

    public PayedGroup(UserRepository userRepository, User user, String groupName,
                      String payingUser, double amount, String reason) {
        super(userRepository, user);
        this.groupName = groupName;
        this.amount = amount;
        this.reason = reason;
        this.payingUser = payingUser;
    }

    @Override
    public String execute() {
        try {
            userRepository.payedGroup(payingUser, groupName , user, amount, reason);
        } catch (NegativeAmountException | GroupNotFoundException
                 | NotMemberOfGroupException | NoDueAmountFoundException
                 | UserNotFoundException | NoDueAmountOwedByException | NoDueAmountOwedToException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, user.getUsername(), e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return String.format(MESSAGE, payingUser, amount, reason, groupName);
    }
}
