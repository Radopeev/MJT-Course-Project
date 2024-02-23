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
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.InvalidCommandException;
import bg.uni.sofia.fmi.mjt.splitwise.repository.UserRepository;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class CommandBuilderTest {
    private UserRepository userRepository;
    private Optional<User> user;

    @BeforeEach
    void setup(){
        userRepository = Mockito.mock();
        user = Optional.empty();
    }

    @Test
    void testNewCommandWhenCommandIsLogin() throws InvalidCommandException {
        String login = "login rado rado";
        Command command = CommandBuilder.newCommand(login, userRepository, user);

        assertInstanceOf(Login.class, command, "Command should be an instance of Login");
    }
    @Test
    void testNewCommandWhenCommandIsLoginAndInvalid() {
        String shorter = "login rado";

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter, userRepository, Optional.empty()),
            "New command should throw an exception when login is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsRegister() throws InvalidCommandException {
        String register = "register rado rado";
        Command command = CommandBuilder.newCommand(register, userRepository, user);

        assertInstanceOf(Register.class, command, "Command should be an instance of Register");
    }
    @Test
    void testNewCommandWhenCommandIsRegisterAndInvalid() {
        String shorter = "register rado";

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter, userRepository,Optional.empty()),
            "New command should throw an exception when register is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsAddFriend() throws InvalidCommandException {
        String addFriend = "add-friend rado";
        user = Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(addFriend, userRepository, user);

        assertInstanceOf(AddFriend.class, command, "Command should be an instance of AddFriend");
    }
    @Test
    void testNewCommandWhenCommandIsAddFriendAndInvalid() {
        String shorter = "add-friend";
        user = Optional.of(new User("rado", "rado"));

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter, userRepository, user),
            "New command should throw an exception when add-friend is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsCreateGroup() throws InvalidCommandException {
        String createGroup = "create-group roommates gosho stefcho mitko";
        when(userRepository.getUser("gosho")).thenReturn(new User("gosho","gosho"));
        when(userRepository.getUser("stefcho")).thenReturn(new User("stefcho","stefcho"));
        when(userRepository.getUser("mitko")).thenReturn(new User("mitko","mitko"));
        user = Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(createGroup, userRepository, user);

        assertInstanceOf(CreateGroup.class, command, "Command should be an instance of CreateGroup");
    }

    @Test
    void testNewCommandWhenCommandIsCreateGroupAndInvalid() {
        String shorter = "create-group roommates ";
        user = Optional.of(new User("rado", "rado"));

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter, userRepository, user),
            "New command should throw an exception when create-group is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsGetStatus() throws InvalidCommandException {
        String getStatus = "get-status";
        user = Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(getStatus, userRepository, user);

        assertInstanceOf(GetStatus.class, command, "Command should be an instance of GetStatus");
    }

    @Test
    void testNewCommandWhenCommandIsPayed() throws InvalidCommandException {
        String payed = "payed 5 lemons misho";
        when(userRepository.getUser("misho")).thenReturn(new User("misho","misho"));
        user = Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(payed, userRepository, user);

        assertInstanceOf(Payed.class, command, "Command should be an instance of Payed");
    }
    @Test
    void testNewCommandWhenCommandIsPayedAndInvalid() {
        String shorter = "payed 5 lemons ";
        user = Optional.of(new User("rado", "rado"));

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter, userRepository, user),
            "New command should throw an exception when payed is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsSplitGroup() throws InvalidCommandException {
        String splitGroup = "split-group 5 roommates rent";
        user = Optional.of(new User("rado", "rado"));
        Command command = CommandBuilder.newCommand(splitGroup, userRepository, user);

        assertInstanceOf(SplitGroup.class, command , "Command should be an instance of SplitGroup");
    }

    @Test
    void testNewCommandWhenCommandIsSplitGroupAndInvalid() {
        String shorter = "split-group 5 roommates";
        user = Optional.of(new User("rado", "rado"));

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter, userRepository, user),
            "New command should throw an exception when split-group is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsSplit() throws InvalidCommandException {
        String split = "split 5 gosho lemons";
        when(userRepository.getUser("gosho")).thenReturn(new User("gosho","gosho"));
        user = Optional.of(new User("rado","rado"));
        Command command = CommandBuilder.newCommand(split, userRepository, user);

        assertInstanceOf(Split.class, command, "Command should be an instance of Split");
    }

    @Test
    void testNewCommandWhenCommandIsSplitAndInvalid() {
        String shorter = "split 5 rado ";
        user = Optional.of(new User("rado","rado"));

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter,userRepository,user),
            "New command should throw na exception when split is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsHelpAndUserIsLogged() throws InvalidCommandException {
        String help = "help";
        user = Optional.of(new User("rado","rado"));
        Command command = CommandBuilder.newCommand(help, userRepository, user);

        assertInstanceOf(HelpLoggedUser.class, command, "Command should be an instance of help");
    }

    @Test
    void testNewCommandWhenCommandIsHelpAndUserIsNotLogged() throws InvalidCommandException {
        String help = "help";
        Command command = CommandBuilder.newCommand(help, userRepository, user);

        assertInstanceOf(HelpNotLoggedUser.class, command, "Command should be an instance of help");
    }

    @Test
    void testNewCommandWhenCommandIsLogout() throws InvalidCommandException {
        String help = "logout";
        user = Optional.of(new User("rado","rado"));
        Command command = CommandBuilder.newCommand(help, userRepository, user);

        assertInstanceOf(Logout.class, command, "Command should be an instance of logout");
    }

    @Test
    void testNewCommandWhenCommandIsPayedGroupAndIsInvalid() {
        String shorter = "payed-group family dobo 5 ";
        user = Optional.of(new User("rado","rado"));

        assertThrows(InvalidCommandException.class, () -> CommandBuilder.newCommand(shorter,userRepository,user),
            "New command should throw an exception when payed-group is invalid");
    }

    @Test
    void testNewCommandWhenCommandIsPayedGroupAndInvalid() throws InvalidCommandException {
        String payedGroup = "payed-group family dobo 5 lemons";
        when(userRepository.getUser("dobo")).thenReturn(new User("dobo","dobo"));
        user = Optional.of(new User("rado","rado"));
        Command command = CommandBuilder.newCommand(payedGroup, userRepository, user);

        assertInstanceOf(PayedGroup.class, command, "Command should be an instance of PayedGroup");
    }

    @Test
    void testNewCommandWhenCommandIsUnknown() throws InvalidCommandException {
        String payedGroup = "alabala family dobo 5 lemons";
        Command command = CommandBuilder.newCommand(payedGroup, userRepository, user);

        assertInstanceOf(UnknownCommand.class, command, "Command should be an instance of UnknownCommand");
        user = Optional.of(new User("rado","rado"));
        command = CommandBuilder.newCommand(payedGroup, userRepository, user);
        assertInstanceOf(UnknownCommand.class, command, "Command should be an instance of UnknownCommand");
    }

    @Test
    void testNewCommandWhenCommandIsHistory() throws InvalidCommandException {
        String payedGroup = "history";
        user = Optional.of(new User("rado","rado"));
        Command command = CommandBuilder.newCommand(payedGroup, userRepository, user);

        assertInstanceOf(History.class, command, "Command should be an instance of History");
    }

    @Test
    void testNewCommandWhenCommandIsGetTotalAmount() throws InvalidCommandException {
        String getTotalAmount = "get-total-amount friend owedto dobo";
        user = Optional.of(new User("rado","rado"));
        Command command = CommandBuilder.newCommand(getTotalAmount, userRepository, user);

        assertInstanceOf(GetTotalAmount.class, command, "Command should be an istance of GetTotalAmount");
    }
}