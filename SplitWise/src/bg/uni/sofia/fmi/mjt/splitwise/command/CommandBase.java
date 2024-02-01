package bg.uni.sofia.fmi.mjt.splitwise.command;

import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public abstract class CommandBase implements Command {
    protected UserRepository userRepository;
    protected User user;

    public CommandBase(UserRepository userRepository,User user){
        this.userRepository = userRepository;
        this.user = user;
    }

}
