package bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

import java.util.Set;

public class CreateGroup extends CommandBase {

    private Set<User> members;
    private String groupName;
    public CreateGroup(UserRepository userRepository, User user, Set<User> members,String groupName){
        super(userRepository, user);
        this.groupName = groupName;
        this.members = members;
    }
    @Override
    public String execute() {
        userRepository.createGroup(members,groupName);
        return String.format("You successfully created %s group",groupName);
    }
}
