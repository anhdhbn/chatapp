package npclient.core;

import nputils.Constants;
import nputils.DataTransfer;

import java.io.*;
import java.net.Socket;

public class Connection extends Socket {

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

    public Connection() throws IOException {
        this(NP_HOST, NP_PORT);
    }

    public Connection(String host, int port) throws IOException {
        super(host, port);
    }
}
