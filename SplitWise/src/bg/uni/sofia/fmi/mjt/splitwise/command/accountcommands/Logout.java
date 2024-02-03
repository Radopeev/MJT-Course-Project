package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

public class Logout implements Command {

    @Override
    public String execute() {
        return "You successfully logged out" + System.lineSeparator();
    }
}
