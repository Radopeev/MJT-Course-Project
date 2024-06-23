package bg.uni.sofia.fmi.mjt.splitwise.command;

import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Login;
import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Logout;
import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Register;
import bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands.AddFriend;
import bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands.Payed;
import bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands.CreateGroup;
import bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands.PayedGroup;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.GetStatus;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.GetTotalAmount;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.HelpLoggedUser;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.HelpNotLoggedUser;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.History;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.UnknownCommand;
import bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands.Split;
import bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands.SplitGroup;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.ExceptionSaver;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.InvalidCommandException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.SplitWiseException;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class CommandBuilder {
    private static final int MIN_LENGTH_PAYED_GROUP = 5;
    private static final int MIN_LENGTH_LOGIN = 3;
    private static final int MIN_LENGTH_REGISTER = 3;
    private static final int MIN_LENGTH_ADD_FRIEND = 2;
    private static final int MIN_LENGTH_CREATE_GROUP = 3;
    private static final int MIN_LENGTH_PAYED = 4;
    private static final int MIN_LENGTH_SPLIT = 4;
    private static final int MIN_LENGTH_SPLIT_GROUP = 4;
    private static final int MIN_LENGTH_GET_TOTAL_AMOUNT = 4;
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;
    private static final int INDEX_THREE = 3;
    private static final int INDEX_FOUR = 4;

    public static Command newCommand(String commandLine, UserRepository userRepository, Optional<User> user)
        throws InvalidCommandException {
        String[] separated = commandLine.split("\\s+");
        CommandType command;

        if (separated.length > 0) {
            try {
                String sanitizedCommand = separated[0].toUpperCase().replace("-", "_");
                command = CommandType.valueOf(sanitizedCommand);
            } catch (IllegalArgumentException e) {
                command = CommandType.UNKNOWN_COMMAND;
            }
        } else {
            command = CommandType.UNKNOWN_COMMAND;
        }


        if (user.isEmpty()) {
            return buildCommandWhenUserIsNotLoggedIn(command,separated, userRepository);
        } else {
            return buildCommandWhenUserIsLoggedIn(command,separated, userRepository, user.get());
        }
    }

    private static Command buildCommandWhenUserIsNotLoggedIn(CommandType command,String[] args, UserRepository userRepository)
        throws InvalidCommandException {
        return switch (command) {
            case LOGIN -> buildLogin(args, userRepository);
            case REGISTER -> buildRegister(args, userRepository);
            case HELP_LOGGED_USER -> buildHelpNotLoggedUser();
            default -> new UnknownCommand();
        };
    }

    private static Command buildCommandWhenUserIsLoggedIn(CommandType command, String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        return switch (command) {
            case ADD_FRIEND -> buildAddFriend(args, userRepository, user);
            case CREATE_GROUP -> buildCreateGroup(args, userRepository, user);
            case GET_STATUS -> buildGetStatus(userRepository, user);
            case PAYED -> buildPayed(args, userRepository, user);
            case PAYED_GROUP -> buildPayedGroup(args, userRepository, user);
            case SPLIT_GROUP-> buildSplitGroup(args, userRepository, user);
            case SPLIT -> buildSplit(args, userRepository, user);
            case HELP_NOT_LOGGED_USER -> buildHelpLoggedUser();
            case LOGOUT -> buildLogout();
            case HISTORY -> buildHistory(userRepository, user);
            case GET_TOTAL_AMOUNT -> buildGetTotalAmount(args, userRepository, user);
            default -> new UnknownCommand();
        };
    }

    private static Command buildGetTotalAmount(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_GET_TOTAL_AMOUNT, args);
        boolean isGroup = args[INDEX_ONE].equals("group");
        boolean isOwedTo = args[INDEX_TWO].equals("owedto");
        String friendOrGroup = args[INDEX_THREE];
        return new GetTotalAmount(userRepository, user, friendOrGroup, isGroup, isOwedTo);
    }

    private static Command buildLogout() {
        return new Logout();
    }

    private static Command buildHelpLoggedUser() {
        return new HelpLoggedUser();
    }

    private static Command buildHelpNotLoggedUser() {
        return new HelpNotLoggedUser();
    }

    private static void validateCommand(int requiredLength, String[] args) throws InvalidCommandException {
        if (args.length < requiredLength) {
            throw new InvalidCommandException("The command was not in the correct format");
        }
    }

    private static double validateAmount(String args) throws InvalidCommandException {
        try {
            return Double.parseDouble(args);
        } catch (NumberFormatException e) {
            ExceptionSaver.saveException(new SplitWiseException(e, "No user", e.getStackTrace()));
            throw new InvalidCommandException("You switched amount and username");
        }
    }

    private static Command buildPayedGroup(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_PAYED_GROUP, args);
        String groupName = args[INDEX_ONE];
        String payingUser = args[INDEX_TWO];
        double amount = validateAmount(args[INDEX_THREE]);
        String reason = args[INDEX_FOUR];
        return new PayedGroup(userRepository, user, groupName, payingUser, amount, reason);
    }

    private static Command buildLogin(String[] args, UserRepository userRepository) throws InvalidCommandException {
        validateCommand(MIN_LENGTH_LOGIN, args);
        return new Login(userRepository, args[INDEX_ONE], args[INDEX_TWO]);
    }

    private static Command buildRegister(String[] args, UserRepository userRepository) throws InvalidCommandException {
        validateCommand(MIN_LENGTH_REGISTER, args);
        return new Register(userRepository, args[INDEX_ONE], args[INDEX_TWO]);
    }

    private static Command buildAddFriend(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_ADD_FRIEND, args);
        return new AddFriend(userRepository, user, args[INDEX_ONE]);
    }

    private static Command buildCreateGroup(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_CREATE_GROUP, args);
        String groupName = args[INDEX_ONE];
        Set<String> members = new LinkedHashSet<>(Arrays.asList(args).subList(INDEX_TWO, args.length));
        members.add(user.getUsername());
        return new CreateGroup(userRepository, user, members, groupName);
    }

    private static Command buildGetStatus(UserRepository userRepository, User user) {
        return new GetStatus(userRepository, user);
    }

    private static Command buildPayed(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_PAYED, args);
        double amount = validateAmount(args[INDEX_ONE]);
        String payingUser = args[INDEX_TWO];
        String reason = String.join(" ", Arrays.asList(args).subList(INDEX_THREE, args.length));
        return new Payed(userRepository, user, payingUser, amount, reason);
    }

    private static Command buildSplit(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_SPLIT, args);
        double amount = validateAmount(args[INDEX_ONE]);
        String friend = args[INDEX_TWO];
        String reason = String.join(" ", Arrays.asList(args).subList(INDEX_THREE, args.length));
        return new Split(userRepository, user, friend, amount, reason);
    }

    private static Command buildSplitGroup(String[] args, UserRepository userRepository, User user)
        throws InvalidCommandException {
        validateCommand(MIN_LENGTH_SPLIT_GROUP, args);
        double amount = validateAmount(args[INDEX_ONE]);
        String groupName = args[INDEX_TWO];
        String reason = String.join(" ", Arrays.asList(args).subList(INDEX_THREE, args.length));
        return new SplitGroup(userRepository , user, groupName, amount, reason);
    }

    private static Command buildHistory(UserRepository userRepository, User user) {
        return new History(userRepository, user);
    }
}
