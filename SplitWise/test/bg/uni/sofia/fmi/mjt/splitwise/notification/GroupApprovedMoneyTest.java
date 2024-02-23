package bg.uni.sofia.fmi.mjt.splitwise.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupApprovedMoneyTest {

    @Test
    public void testHashCode() {
        String receiver = "rado";
        String groupName = "group1";

        GroupApprovedMoney notification1 = new GroupApprovedMoney(receiver, "lemons",10, groupName);
        GroupApprovedMoney notification2 = new GroupApprovedMoney(receiver, "lemons",10, groupName);

        assertEquals(notification1.hashCode(), notification2.hashCode(),"Hashcodes should be equal");
    }
}