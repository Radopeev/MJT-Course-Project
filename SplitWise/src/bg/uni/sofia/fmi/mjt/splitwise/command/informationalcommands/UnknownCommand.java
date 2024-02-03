package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;

public class UnknownCommand implements Command {

    @Override
    public String execute() {
        return "Unknown command! Please try again or press 1 for help" + System.lineSeparator();
    }
}
