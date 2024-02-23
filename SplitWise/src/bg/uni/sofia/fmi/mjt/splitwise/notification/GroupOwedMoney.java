package bg.uni.sofia.fmi.mjt.splitwise.notification;

import java.util.Objects;

public class GroupOwedMoney extends OwedMoneyNotification {
    private String groupName;
    private String members;
    public GroupOwedMoney( String reason, double amount, String groupName, String members) {
        super(reason, amount);
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public String inform() {
        return String.format("You owe %s %s LV [%s] in %s",
            members, amount, reason, groupName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupOwedMoney that = (GroupOwedMoney) o;
        return Objects.equals(groupName, that.groupName) && Objects.equals(members, that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, members);
    }
}
