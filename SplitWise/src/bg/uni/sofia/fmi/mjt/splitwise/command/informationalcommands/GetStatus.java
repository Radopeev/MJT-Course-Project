package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class GetStatus extends CommandBase {
    public GetStatus(UserRepository userRepository, User user) {
        super(userRepository, user);
    }

    @Override
    public String execute() {
        return userRepository.getStatus(user) + System.lineSeparator();
    }
}
