package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Login extends CommandBase {
    public Login(UserRepository userRepository, User user){
        super(userRepository,user);
    }
    @Override
    public String execute() {
        if(!userRepository.contains(user)){
            // TODO : throw exception
        }
        return "You successfully logged in";
    }

}
