package bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands;

import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExists;
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
        try {
            userRepository.createGroup(members, groupName);
        } catch(GroupAlreadyExists e){
            return e.getMessage();
        }
        return String.format("You successfully created %s group" + System.lineSeparator(),groupName);
    }
}
