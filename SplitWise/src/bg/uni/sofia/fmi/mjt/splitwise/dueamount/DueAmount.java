package bg.uni.sofia.fmi.mjt.splitwise.dueamount;

public class DueAmount {
    private double amount;
    private String reason;

    public DueAmount(double amount, String reason){
        this.amount = amount;
        this.reason = reason;
    }

    public double getAmount(){
        return amount;
    }
    public String getReason(){
        return reason;
    }
    public void updateAmount(double amount){
        this.amount -= amount;
    }
}
