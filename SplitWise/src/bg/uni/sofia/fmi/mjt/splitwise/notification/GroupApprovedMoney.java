package bg.uni.sofia.fmi.mjt.splitwise.notification;

import java.util.Objects;

public class GroupApprovedMoney extends ApprovedMoneyNotification {
    private String groupName;
    public GroupApprovedMoney(String receiver, String reason, double amount, String groupName) {
        super(receiver, reason, amount);
        this.groupName = groupName;
    }

    @Override
    public String inform() {
        return String.format("Your payment of %s in %s [%s] has been approved by %s",
            amount, groupName, reason, receiver);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupApprovedMoney that = (GroupApprovedMoney) o;
        return Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }
}
