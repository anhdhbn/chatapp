package npclient.core;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static npclient.Constants.NP_HOST;
import static npclient.Constants.TCP_PORT;

public class TCPConnection extends Socket {

    /**
     * Local case
     */
    private static final int DEFAULT_PORT = 13;
    private static final String LOCAL_HOST = "127.0.0.1";

    public TCPConnection() throws IOException {
        this(NP_HOST, TCP_PORT);
    }

    public TCPConnection(String host, int port) throws IOException {
        super(host, port);
    }
}
