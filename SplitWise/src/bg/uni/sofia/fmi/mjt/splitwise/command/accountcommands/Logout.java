package bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;

public class Logout implements Command {
    private static final String MESSAGE = "You have successfully logged out" + System.lineSeparator();

    @Override
    public String execute() {
        return MESSAGE;
    }
}
