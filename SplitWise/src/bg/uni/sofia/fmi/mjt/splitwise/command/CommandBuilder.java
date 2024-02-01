package bg.uni.sofia.fmi.mjt.splitwise.command;

import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Login;
import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Register;
import bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands.AddFriend;
import bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands.Payed;
import bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands.CreateGroup;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.GetStatus;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.UnknownCommand;
import bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands.Split;
import bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands.SplitGroup;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CommandBuilder {
    public static Command newCommand(String command, UserRepository userRepository, Optional<User> user) {
        String[] separated = command.split(" ");
        if(user.isEmpty()) {
            switch (separated[0]) {
                case "login" -> {
                    return buildLogin(separated, userRepository);
                }
                case "register" -> {
                    return buildRegister(separated, userRepository);
                }
                default -> {
                    return new UnknownCommand();
                }
            }
        }
        switch (separated[0]){
            case "add-friend" -> {
                return buildAddFriend(separated,userRepository,user.get());
            }
            case "create-group" -> {
                return buildCreateGroup(separated,userRepository,user.get());
            }
            case "get-status" -> {
                return buildGetStatus(userRepository,user.get());
            }
            case "payed" -> {
                return buildPayed(separated,userRepository,user.get());
            }
            case "split-group" -> {
                return buildSplitGroup(separated, userRepository, user.get());
            }
            case "split" -> {
                return buildSplit(separated,userRepository,user.get());
            }
        }
        return new UnknownCommand();
    }

    private static Command buildLogin(String[] args, UserRepository userRepository) {
        User user = new User(args[1], args[2]);
        return new Login(userRepository, user);
    }

    private static Command buildRegister(String[] args, UserRepository userRepository) {
        User newUser = new User(args[1], args[2]);
        return new Register(userRepository,newUser);
    }
    private static Command buildAddFriend(String[] args, UserRepository userRepository,User user){
        return new AddFriend(userRepository,user,args[1]);
    }

    private static Command buildCreateGroup(String[] args, UserRepository userRepository,User user){
        String groupName = args[1];
        Set<User> members = new HashSet<>();
        for(int i=2;i<args.length;i++){
            members.add(userRepository.getUser(args[i]));
        }
        members.add(user);
        return new CreateGroup(userRepository,user,members,groupName);
    }

    private static Command buildGetStatus(UserRepository userRepository, User user){
        return new GetStatus(userRepository,user);
    }

    private static Command buildPayed(String[] args,UserRepository userRepository,User user){
        double amount = Double.parseDouble(args[1]);
        User payingUser = userRepository.getUser(args[2]);
        return new Payed(userRepository,user,payingUser,amount);
    }

    private static Command buildSplit(String[] args, UserRepository userRepository, User user){
        double amount = Double.parseDouble(args[1]);
        User friend = userRepository.getUser(args[2]);
        String reason = args[3];
        return new Split(userRepository, user, friend, amount, reason);
    }
    private static Command buildSplitGroup(String[] args,UserRepository userRepository,User user){
        double amount = Double.parseDouble(args[1]);
        String groupName = args[2];
        String reason = args[3];
        return new SplitGroup(userRepository,user,groupName,amount,reason);
    }
}
