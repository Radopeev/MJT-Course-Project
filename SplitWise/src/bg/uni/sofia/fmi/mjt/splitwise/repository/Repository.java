package bg.uni.sofia.fmi.mjt.splitwise.repository;

import bg.uni.sofia.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.FriendNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupAlreadyExistsException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.GroupNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NegativeAmountException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedByException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NoDueAmountOwedToException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.NotMemberOfGroupException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UserNotFoundException;
import bg.uni.sofia.fmi.mjt.splitwise.exceptions.UsernameAlreadyTakenException;
import bg.uni.sofia.fmi.mjt.splitwise.user.payment.SplitHistoryEntry;
import bg.uni.sofia.fmi.mjt.splitwise.user.User;

import java.util.List;
import java.util.Set;

public interface Repository {
    public void addUser(String username, String password) throws UsernameAlreadyTakenException;

    public void findUser(String username, String password) throws UserNotFoundException;

    public void addFriendToUser(User user, String friend) throws UserNotFoundException, AlreadyFriendsException;

    public void createGroup(Set<String> membersUsernames, String groupName)
        throws GroupAlreadyExistsException, UserNotFoundException;

    public void split(String friendUsername, User user, double amount, String reason)
        throws FriendNotFoundException, NegativeAmountException, UserNotFoundException;

    public void splitGroup(String groupName, User user, double amount, String reason)
        throws NotMemberOfGroupException, GroupNotFoundException, NegativeAmountException;

    public String getStatus(User user);

    public void payed(String payingUsername, double amount, User user, String reason)
        throws FriendNotFoundException, NegativeAmountException, NoDueAmountFoundException, UserNotFoundException,
        NoDueAmountOwedToException, NoDueAmountOwedByException;

    public void payedGroup(String payingUserUsername, String groupName, User user, double amount, String reason)
        throws NegativeAmountException, GroupNotFoundException, NotMemberOfGroupException, NoDueAmountFoundException,
        UserNotFoundException, NoDueAmountOwedToException, NoDueAmountOwedByException;

    public List<SplitHistoryEntry> getPaymentHistory(User user);

    public double getTotalAmount(User user, String target, boolean isGroup, boolean isOwedTo);
}