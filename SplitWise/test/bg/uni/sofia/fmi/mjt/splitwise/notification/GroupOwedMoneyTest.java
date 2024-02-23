package bg.uni.sofia.fmi.mjt.splitwise.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupOwedMoneyTest {

    @Test
    public void testHashCode() {
        String groupName = "group1";
        String members = "rado, dobo";

        GroupOwedMoney notification1 = new GroupOwedMoney("lemons",10, groupName, members);
        GroupOwedMoney notification2 = new GroupOwedMoney("lemons",10, groupName, members);

        assertEquals(notification1.hashCode(), notification2.hashCode(),"Hashcodes should be equal");
    }
}
