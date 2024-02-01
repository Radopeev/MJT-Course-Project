package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class SplitGroup extends CommandBase {
    private String groupName;
    private double amount;
    private String reason;

    public SplitGroup(UserRepository userRepository, User user, String groupName, double amount, String reason) {
        super(userRepository, user);
        this.groupName = groupName;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public String execute() {
        userRepository.splitGroup(groupName, user, amount, reason);
        return null;
    }
}
