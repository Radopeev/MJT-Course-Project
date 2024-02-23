package bg.uni.sofia.fmi.mjt.splitwise.command;

public enum CommandType {
    LOGIN("login"),
    LOGOUT("logout"),
    REGISTER("register"),
    ADD_FRIEND("add-friend"),
    PAYED("payed"),
    CREATE_GROUP("create-group"),
    PAYED_GROUP("payed-group"),
    GET_STATUS("get-status"),
    GET_TOTAL_AMOUNT("get-total-amount"),
    HELP_LOGGED_USER("help"),
    HELP_NOT_LOGGED_USER("help"),
    HISTORY("history"),
    UNKNOWN_COMMAND(""),
    SPLIT("split"),
    SPLIT_GROUP("split-group");

    private String value;

    CommandType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
