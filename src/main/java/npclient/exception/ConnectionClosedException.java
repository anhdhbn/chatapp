package npclient.exception;

public class ConnectionClosedException extends Exception {
    public ConnectionClosedException() {
        super("Connection Closed!");
    }
}
