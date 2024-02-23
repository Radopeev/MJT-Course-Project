package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;

public class HelpLoggedUser implements Command {
    private static final String AVAILABLECOMMANDS = """
                To add new friend type :
                    add-friend <username>
                To create new group type :
                    create-group <group_name> <username> <username> ... <username>
                To split new bill with a friend type :
                    split <amount> <username> <reason_for_payment>
                To split new bill with a group type :
                    split-group <amount> <group_name> <reason_for_payment>
                To get your current status type :
                    get-status
                То update that someone has repaid you type :
                    payed <amount> <username> <reason>
                To update that someone has repaid you in a specific group type :
                    payed-group <groupname> <username> <amount> <reason>
                To disconnect from the app type :
                    quit
                To see the history of your splits type :
                    history
                To see how much are owed or owe to group or friend type :
                    get-total-amount <'group'/'friend'> <'owedto'/'owedby'> <groupname/username>
                """;
    @Override
    public String execute() {
        return AVAILABLECOMMANDS;
    }
}
