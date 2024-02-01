package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

public class AddFriend extends CommandBase {
    private String friend;
    public AddFriend(UserRepository userRepository, User user, String friend){
        super(userRepository,user);
        this. friend =friend;
    }
    @Override
    public String execute() {
        userRepository.addFriendToUser(user,friend);
        return String.format("You successfully added %s as your friend",friend);
    }
}
