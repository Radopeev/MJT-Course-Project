package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Register extends CommandBase {

    public Register(UserRepository userRepository, User user){
        super(userRepository,user);
    }
    @Override
    public String execute() {
        userRepository.addUser(user);
        return "You have successfully registered";
    }
}
