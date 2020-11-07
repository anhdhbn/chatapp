package npclient.core;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPConnection extends Socket {

    /**
     * Local case
     */
    private static final int DEFAULT_PORT = 13;
    private static final String LOCAL_HOST = "127.0.0.1";

    /**
     * CI/CD server case
     */
    private static final int NP_PORT = 1699;
    private static final String NP_HOST = "np-server.anhdh.me";

    public TCPConnection() throws IOException {
        this(NP_HOST, NP_PORT);
    }

    public TCPConnection(String host, int port) throws IOException {
        super(host, port);
    }
}
