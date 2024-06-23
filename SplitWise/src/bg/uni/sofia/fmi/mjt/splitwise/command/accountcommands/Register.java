package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Register implements Command {
    private UserRepository userRepository;
    private String username;
    private String password;
    private static final String MESSAGE =  "You have successfully registered" + System.lineSeparator();

    public Register(UserRepository userRepository, String username, String password) {
        this.userRepository = userRepository;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {
        try {
            userRepository.addUser(username, password);
        } catch (UsernameAlreadyTakenException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, username, e.getStackTrace()));
            return e.getMessage() + System.lineSeparator();
        }
        return MESSAGE;
    }
}
