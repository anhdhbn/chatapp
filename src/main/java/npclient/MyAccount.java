package npclient;

public class MyAccount {

    private String name;
    private boolean inCall;

    private static MyAccount instance;

    public static void register(String name) {
        instance = new MyAccount(name);
//        return instance;
    }

    public static MyAccount getInstance() {
        return instance;
    }

    public MyAccount() {
        this.inCall = false;
    }

    public MyAccount(String name) {
        this();
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInCall() {
        return inCall;
    }

    public void setInCall(boolean inCall) {
        this.inCall = inCall;
    }
}
