package bg.uni.sofia.fmi.mjt.splitwise.user.payment;

public enum PaymentType {
    GROUP("Group"),
    FRIEND("Friend");

    private String value;

    PaymentType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
