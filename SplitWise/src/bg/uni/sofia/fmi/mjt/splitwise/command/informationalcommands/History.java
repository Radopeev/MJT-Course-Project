package bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.SplitHistoryEntry;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.util.List;

public class History extends CommandBase {
    public History(UserRepository userRepository,
                   User user) {
        super(userRepository, user);
    }

    @Override
    public String execute() {
        List<SplitHistoryEntry> historyEntries = userRepository.getPaymentHistory(user);
        StringBuilder history = new StringBuilder();
        for (SplitHistoryEntry entry : historyEntries) {
            history.append(entry.toString()).append(System.lineSeparator());
        }
        return history.toString();
    }
}
