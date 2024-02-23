package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;

public class HelpNotLoggedUser implements Command {
    private static final String AVAILABLECOMMANDS = """
            To register type: register <username> <password>,
            To login type: login <username> <password>;
        """;

    @Override
    public String execute() {
        return AVAILABLECOMMANDS;
    }
}
