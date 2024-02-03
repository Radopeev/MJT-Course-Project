package bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFound;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class Split extends CommandBase {

    private User friend;
    private double amount;
    private String reason;
    public Split(UserRepository userRepository, User user,User friend, double amount, String reason) {
        super(userRepository, user);
        this.friend = friend;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public String execute() {
        try {
            userRepository.split(friend, user, amount, reason);
        } catch(UserNotFound | FriendNotFound e){
            return e.getMessage();
        }
        return String.format("You have split %s LV with %s",amount,friend.getUsername());
    }
}
