package npclient.core;

import java.io.*;
import java.net.Socket;

public class Connection {

    private static final int DEFAULT_PORT = 13;
    private static final String LOCAL_HOST = "127.0.0.1";

    private final Socket socket;
    private final InputStream inputStream;
    private final Writer writer;

    public Connection() throws IOException {
        this(LOCAL_HOST, DEFAULT_PORT);
    }

    public Connection(String host, int port) throws IOException {
        /*
         * Initialize socket
         */
        socket = new Socket(host, port);

        /*
         * get input stream from socket
         */
        inputStream = socket.getInputStream();

        /*
         * Initialize write from output stream
         */
        OutputStream outputStream = socket.getOutputStream();
        writer = new OutputStreamWriter(outputStream);
    }

    public Writer getWriter() {
        return writer;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public Socket getSocket() {
        return socket;
    }
}
