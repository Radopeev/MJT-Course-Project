package bg.uni.sofia.fmi.mjt.spiltwise.command;

import bg.uni.sofia.fmi.mjt.splitwise.command.Command;
import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBase;
import bg.uni.sofia.fmi.mjt.splitwise.command.CommandBuilder;
import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Login;
import bg.uni.sofia.fmi.mjt.splitwise.command.accountcommands.Register;
import bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands.AddFriend;
import bg.uni.sofia.fmi.mjt.splitwise.command.friendcommands.Payed;
import bg.uni.sofia.fmi.mjt.splitwise.command.groupcommands.CreateGroup;
import bg.uni.sofia.fmi.mjt.splitwise.command.informationalcommands.GetStatus;
import bg.uni.sofia.fmi.mjt.splitwise.command.splitcommands.SplitGroup;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.InvalidCommand;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandBuilderTest {
    private UserRepository userRepository;
    private Optional<User> user;
    private CommandBuilder commandBuilder;
    @BeforeEach
    void setup(){
        userRepository = Mockito.mock();
        user = Optional.empty();
    }

    @Test
    void newCommandWhenCommandIsLogin() throws InvalidCommand {
        String login = "login rado rado";
        Command command = CommandBuilder.newCommand(login,userRepository,user);

        assertInstanceOf(Login.class, command);
    }
    @Test
    void newCommandWhenCommandIsRegister() throws InvalidCommand {
        String register = "register rado rado";
        Command command = CommandBuilder.newCommand(register,userRepository,user);

        assertInstanceOf(Register.class, command);
    }

    @Test
    void newCommandWhenCommandIsAddFriend() throws InvalidCommand {
        String addFriend = "add-friend rado";
        user= Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(addFriend,userRepository,user);

        assertInstanceOf(AddFriend.class,command);
    }

    @Test
    void newCommandWhenCommandIsCreateGroup() throws InvalidCommand {
        String createGroup = "create-group roommates gosho stefcho mitko";
        user= Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(createGroup,userRepository,user);

        assertInstanceOf(CreateGroup.class,command);
    }

    @Test
    void newCommandWhenCommandIsGetStatus() throws InvalidCommand {
        String getStatus = "get-status";
        user= Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(getStatus,userRepository,user);

        assertInstanceOf(GetStatus.class,command);
    }

    @Test
    void newCommandWhenCommandIsPayed() throws InvalidCommand {
        String payed = "payed 5 misho";
        user= Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(payed,userRepository,user);

        assertInstanceOf(Payed.class,command);
    }

    @Test
    void newCommandWhenCommandIsSplitGroup() throws InvalidCommand {
        String splitGroup = "split-group 5 roommates rent";
        user= Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(splitGroup,userRepository,user);

        assertInstanceOf(SplitGroup.class,command);
    }

}
