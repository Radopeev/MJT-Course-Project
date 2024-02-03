package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;

import java.util.List;
import java.util.stream.Collectors;

public class Help implements Command {

    private static final List<String> availableCommands= List.of(
        "To register type: register <username> <password>",
        "To login type: login <username> <password>",
        "To add new friend type: add-friend <username>",
        "To create new group type: create-group <group_name> <username> <username> ... <username>",
        "To split new bill with a friend type: split <amount> <username> <reason_for_payment>",
        "To split new bill with a group type: split-group <amount> <group_name> <reason_for_payment>",
        "To get your current status type: get-status",
        "То update that someone has repaid you type: payed <amount> <username>"
    );

    @Override
    public String execute() {
        return availableCommands.stream().collect(Collectors.joining(System.lineSeparator()));
    }
}
