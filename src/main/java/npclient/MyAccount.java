package npclient;

import npclient.core.UDPConnection;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyAccount {

    private String name;
    private final AtomicBoolean inCall;
    private UDPConnection udpConn;

    private static MyAccount instance;

    public static void register(String name, UDPConnection udpConn) {
        instance = new MyAccount(name, udpConn);
    }

    public static MyAccount getInstance() {
        return instance;
    }

    public MyAccount() {
        this.inCall = new AtomicBoolean(false);
    }

    public MyAccount(String name, UDPConnection udpConn) {
        this();
        this.name = name;
        this.udpConn = udpConn;
    }

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInCall() {
        return inCall.get();
    }

    public synchronized void setInCall(boolean inCall) {
        this.inCall.set(inCall);
    }

    public UDPConnection getUdpConn() {
        return udpConn;
    }
}
