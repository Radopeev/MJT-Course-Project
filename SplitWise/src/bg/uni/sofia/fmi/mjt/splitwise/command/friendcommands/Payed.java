package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Payed extends CommandBase {
    private User payingUser;
    private double amount;
    public Payed(UserRepository userRepository, User user, User payingUser, double amount) {
        super(userRepository, user);
        this.payingUser = payingUser;
        this.amount = amount;
    }

    @Override
    public String execute() {
        try {
            userRepository.payed(payingUser, amount, user);
        } catch(FriendNotFound e){
            return e.getMessage();
        }
        return String.format("%s payed you %s LV",payingUser.getUsername() + System.lineSeparator(),amount);
    }
}
