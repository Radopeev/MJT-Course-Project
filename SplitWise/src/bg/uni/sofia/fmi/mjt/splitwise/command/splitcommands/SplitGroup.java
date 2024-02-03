package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroup;
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
        try {
            userRepository.splitGroup(groupName, user, amount, reason);
        } catch (GroupNotFound | NotMemberOfGroup e ){
            return e.getMessage();
        }
        return String.format("You have split %s LV with %s",amount,groupName);
    }
}
