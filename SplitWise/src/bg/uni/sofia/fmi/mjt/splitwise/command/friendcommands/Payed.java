package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedByException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedToException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Payed extends CommandBase {
    private String payingUserUsername;
    private double amount;
    private String reason;
    private static final String MESSAGE = "%s has payed you %s LV";
    public Payed(UserRepository userRepository, User user, String payingUserUsername, double amount, String reason) {
        super(userRepository, user);
        this.payingUserUsername = payingUserUsername;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public String execute() {
        try {
            userRepository.payed(payingUserUsername, amount, user, reason);
        } catch (FriendNotFoundException | NegativeAmountException
                 | NoDueAmountFoundException | UserNotFoundException
                 | NoDueAmountOwedByException | NoDueAmountOwedToException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, user.getUsername(), e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return String.format(MESSAGE, payingUserUsername, amount) + System.lineSeparator();
    }
}
