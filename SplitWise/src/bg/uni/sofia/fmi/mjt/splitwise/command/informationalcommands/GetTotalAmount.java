package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

public class GetTotalAmount extends CommandBase {
    private String friendOrGroup;
    private boolean isGroup;
    private boolean isOwedTo;
    public GetTotalAmount(UserRepository userRepository,
                          User user, String friendOrGroup, boolean isGroup, boolean isOwedTo) {
        super(userRepository, user);
        this.friendOrGroup = friendOrGroup;
        this.isGroup = isGroup;
        this.isOwedTo = isOwedTo;
    }

    @Override
    public String execute() {
        return String.format("You %s %s in total%s",
            isOwedTo ?  "owe" : "are owed",
            userRepository.getTotalAmount(user, friendOrGroup, isGroup, isOwedTo),
            System.lineSeparator());
    }
}
