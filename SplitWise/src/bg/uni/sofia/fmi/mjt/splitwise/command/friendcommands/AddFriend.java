package bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriends;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFound;
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
        try {
            userRepository.addFriendToUser(user, friend);
        } catch(UserNotFound | AlreadyFriends e){
            return e.getMessage();
        }
        return String.format("You successfully added %s as your friend" + System.lineSeparator(),friend);
    }
}
