package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;

public class UnknownCommand implements Command {
    private static final String MESSAGE = "Unknown command! Please try again or type help for help"
        + System.lineSeparator();

    @Override
    public String execute() {
        return MESSAGE;
    }
}
