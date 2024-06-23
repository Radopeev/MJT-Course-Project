package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Login implements Command {
    private UserRepository userRepository;
    private String username;
    private String password;
    private static final String MESSAGE = "You have successfully logged in" + System.lineSeparator();
    public Login(UserRepository userRepository, String username, String password) {
        this.userRepository = userRepository;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {
        try {
            userRepository.findUser(username, password);
        } catch (UserNotFoundException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, username, e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return MESSAGE;
    }

}
